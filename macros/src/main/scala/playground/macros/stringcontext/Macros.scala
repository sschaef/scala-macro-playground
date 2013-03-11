package playground.macros.stringcontext

import language.experimental.macros
import scala.reflect.macros.Macro

object Macros {

  sealed trait Piece
  case class Place(str: String) extends Piece
  case class Name(str: String) extends Piece
  case class Pos(column: Int, line: Int)
  case class LocatedPieces(located: List[(String, Piece, Pos)])

  implicit class s2pieces(sc: StringContext) {
    def s2(pieces: Piece*) = macro Impl.s2impl
  }
}

trait Impl extends Macro {

  import Macros._
  import c.universe.{ Name => _, _ }

  // pieces contain all the Piece instances passed inside of the string interpolation
  def s2impl(pieces: c.Expr[Piece]*): c.Expr[LocatedPieces] = {

    c.prefix.tree match {
      // access data of string interpolation
      case Apply(_, List(Apply(_, rawParts))) =>

        // `parts` contain the strings a string interpolation is built of
        val parts = rawParts map { case Literal(Constant(const: String)) => const }
        // translate compiler positions to a data structure that can live outside of the compiler
        val positions = pieces.toList map (_.tree.pos) map (p => Pos(p.column, p.line))
        // discard last element of parts, `transpose` does not work otherwise
        // trim parts to discard unnecessary white space
        val data = List(parts.init map (_.trim), pieces.toList, positions).transpose
        // create an AST containing a List[(String, Piece, Pos)]
        val tupleAST = data map { case List(part: String, piece: c.Expr[_], Pos(column, line)) =>
          toTupleAST(toAST(part), piece.tree, toPosAST(toAST(column), toAST(line)))
        }
        // create an AST of `LocatedPieces`
        val locatedPiecesAST = toLocatedPiecesAST(toListAST(tupleAST))
        c.Expr(locatedPiecesAST)

      case _ =>
        c.abort(c.enclosingPosition, "invalid")
    }
  }

  // helper methods
  def typeIdent[A : TypeTag] =
    Ident(typeTag[A].tpe.typeSymbol)

  def companionIdent[A : TypeTag] =
    Ident(typeTag[A].tpe.typeSymbol.companionSymbol)

  def identFromString(tpt: String) =
    Ident(c.mirror.staticModule(tpt))

  // We need to translate the data calculated inside of the macro to an AST
  // in order to write it back to the compiler.
  def toAST(any: Any) =
    Literal(Constant(any))

  def toPosAST(column: Tree, line: Tree) =
    Apply(
      Select(companionIdent[Pos], TermName("apply")),
      List(column, line))

  def toTupleAST(t1: Tree, t2: Tree, t3: Tree) =
    Apply(
      TypeApply(
        Select(identFromString("scala.Tuple3"), TermName("apply")),
        List(typeIdent[String], typeIdent[Piece], typeIdent[Pos])),
      List(t1, t2, t3))

  def toLocatedPiecesAST(located: Tree) =
    Apply(
      Select(companionIdent[LocatedPieces], TermName("apply")),
      List(located))

  def toListAST(xs: List[Tree]) =
    Apply(
      TypeApply(
        Select(identFromString("scala.collection.immutable.List"), TermName("apply")),
        List(AppliedTypeTree(
          typeIdent[Tuple3[String, Piece, Pos]],
          //identFromString("scala.Tuple3"),
          List(typeIdent[String], typeIdent[Piece], typeIdent[Pos])))),
      xs)
}