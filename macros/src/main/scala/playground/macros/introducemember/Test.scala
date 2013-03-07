package playground.macros.introducemember

import language.experimental.macros
import scala.reflect.macros.Context

class C { def hello() = println("hello world, how are you?") }
case class MacroClass(c: Int)
trait MacroTrait {
  def implicitlyAddedMethod = "it works"
}
trait MacroTrait2 {
  def implicitlyAddedMethod2 = "it works2"
}

object Macros {

  def impl(c: Context) = c.universe.Ident(c.universe.typeOf[C].typeSymbol)
  type TM = macro impl

  type Class(i: Int) = macro type_macro_impl

  def type_macro_impl(c: Context)(i: c.Expr[Int]) = {
    import c.universe._
    /*val f = Template(
      List(Apply(Ident(TypeName("Class")), List(i.tree)), Ident(TypeName("Trait"))),
      emptyValDef,
      List(DefDef(
        Modifiers(),
        nme.CONSTRUCTOR,
        List(),
        List(List()),
        TypeTree(),
        Block(List(pendingSuperCall), Literal(Constant(())))
    ))
    )*/
    //val x = q"object xxx_impl extends Class(${i.tree}) with Trait"

    val f = c.enclosingImpl match {
      case ModuleDef(mods, name, impl @ Template(parents, self, body)) =>
        //c.abort(c.enclosingPosition, List(parents, self, body).mkString("\n>>", "\n\n>>", "\n"))
        Template(
          parents = List(
            Apply(Ident(typeOf[MacroClass].typeSymbol), List(i.tree)),
            //Apply(Ident(TypeName(classOf[MacroClass].getName)), List(i.tree)),
            Ident(typeOf[MacroTrait].typeSymbol),
            Ident(typeOf[MacroTrait2].typeSymbol)
          ) ++ parents.tail,
          self = self,
          body = body
        )
      case _ =>
        c.abort(c.enclosingPosition, s">>>>>>>>>>>\n${showRaw(c.enclosingImpl)}")
    }

    //c.abort(c.enclosingPosition, "\n"+showRaw(c.enclosingImpl))
    //Apply(Ident(TypeName("Class")), List(i.tree))
    
    f
  }

  def addMethod_impl(c: Context)(target: c.Tree, name: c.Tree, code: c.Tree) = {
    import c.universe._
    val Literal(Constant(targetType: Type)) = c.typeCheck(target)
    val Literal(Constant(methodName: String)) = name
    val Function(methodParams, methodBody) = code
    val method = DefDef(NoMods, TermName(methodName), Nil, List(methodParams), TypeTree(), methodBody)
    c.introduceMember(targetType.typeSymbol, method)
    c.literalUnit
  }

  def addMethod(target: _, name: String, code: _) = macro addMethod_impl
}