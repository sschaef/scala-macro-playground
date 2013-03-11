package playground.macros.caseclass

import language.experimental.macros
import scala.reflect.macros.Macro

object Macros {
  def toCaseClass(target: _) = macro Impl.toCaseClass
}

trait Impl extends Macro {

  import c.universe._, Flag._

  def toCaseClass(target: c.Tree) = {

    val Literal(Constant(targetType: Type)) = c.typeCheck(target)
    /*
    val Literal(Constant(methodName: String)) = name
    val Function(methodParams, methodBody) = code
    val method = DefDef(NoMods, TermName(methodName), Nil, List(methodParams), TypeTree(), methodBody)
    c.introduceMember(targetType.typeSymbol, method)
    c.literalUnit
    */
    //parent(targetType)
    c.introduceMember(targetType.typeSymbol, genToString(targetType))
    c.introduceMember(targetType.typeSymbol, genProductArity(targetType))
    c.introduceMember(targetType.typeSymbol, genCanEqual(targetType))
    //info(s"toString rep:\n$str")
    c.literalUnit
  }

  def parent(t: Type) = {
    info(showRaw(c.enclosingImpl))
    c.enclosingImpl match {
      case ModuleDef(mods, name, impl @ Template(parents, self, body)) =>
        info("ok")
        /*Template(
          List(Apply(x, List(i.tree)),
            x,
            x
          ) ++ parents.tail,
          self = self,
          body = body
        )*/
      case _ =>
    }
  }

  def genToString(t: Type) = {
    DefDef(
      Modifiers(OVERRIDE),
      TermName("toString"),
      Nil,
      Nil,
      typeIdent[String],
      Literal(Constant("test"))
      /*Apply(
          Select(
              Select(
                  Select(
                      Ident(newTermName("scala")),
                      newTermName("runtime")),
              newTermName("ScalaRunTime")),
          newTermName("_toString")),
          List(This(tpnme.EMPTY)))*/
    )
  }

  def genCanEqual(t: Type) = {
    DefDef(
      NoMods,
      TermName("canEqual"),
      Nil,
      List(List(ValDef(Modifiers(PARAM), TermName("x$1"), typeIdent[Any], EmptyTree))),
      typeIdent[Boolean],
      TypeApply(
        Select(Ident(TermName("x$1")), TermName("isInstanceOf")),
        List(Ident(t.typeSymbol))
      )
    )
  }

  def genProductArity(t: Type) = {
    DefDef(
      NoMods,
      TermName("productArity"),
      Nil,
      Nil,
      typeIdent[Int],
      Literal(Constant(0))
    )
  }

  def typeIdent[A : TypeTag] =
    Ident(typeTag[A].tpe.typeSymbol)

  def info(s: String) {
  	c.echo(c.enclosingPosition, s"\n====================\n$s\n====================\n")
  }

}