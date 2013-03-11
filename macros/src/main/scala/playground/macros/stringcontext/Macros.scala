package playground.macros.stringcontext

import language.experimental.macros
import scala.reflect.macros.Macro

object Macros {

  // http://stackoverflow.com/questions/15329027/string-interpolation-and-macro-how-to-get-the-stringcontext-and-expression-loca
  sealed trait Piece
  case class Place(str: String) extends Piece
  case class Name(str: String) extends Piece
  case class Pos(column: Int, line: Int)
  case class LocatedPieces(located: Seq[(String, Piece, Pos)])

  implicit class s2pieces(sc: StringContext) {
    def s2(pieces: Piece*) = macro Impl.s2impl
  }
}

trait Impl extends Macro {

  import Macros._
  import c.universe.{ Name => _, _ }

  def s2impl(pieces: c.Expr[Piece]*): c.Expr[LocatedPieces] = {
    val parts = c.prefix.tree match {
      case Apply(_, List(Apply(_, rawParts))) =>
        rawParts zip (pieces map (_.tree)) map {
          case (Literal(Constant(rawPart: String)), piece) =>
            val line = c.literal(piece.pos.line).tree
            val column = c.literal(piece.pos.column).tree
            val part = c.literal(rawPart.trim).tree
            toAST[(_, _, _)](part, piece, toAST[Pos](line, column))
      }
    }
    c.Expr(toAST[LocatedPieces](toAST[Seq[_]](parts: _*)))
  }

  def toAST[A : TypeTag](xs: Tree*): Tree =
    Apply(
      Select(Ident(typeOf[A].typeSymbol.companionSymbol), TermName("apply")),
      xs.toList)
}