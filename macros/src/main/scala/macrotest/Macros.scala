package macrotest

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
  def impl(c: Context) = c.universe.Ident(c.universe.TypeName("C"))
  type TM = macro impl

  type Class(i: Int) = macro xxx_impl

  def xxx_impl(c: Context)(i: c.Expr[Int]) = {
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
            Apply(Ident(TypeName(classOf[MacroClass].getName)), List(i.tree)),
            Ident(TypeName(classOf[MacroTrait].getName)),
            Ident(TypeName(classOf[MacroTrait2].getName))
          ) ++ parents.tail,
          self = self,
          body = body
        )
      case _ =>
        c.abort(c.enclosingPosition, s"wahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\n${showRaw(c.enclosingImpl)}")
    }
    /*
    c.enclosingClass match {
      case ModuleDef(mods, name, impl @ Template(parents, self, body)) =>
        c.abort(c.enclosingPosition, List(parents, self, body).mkString("\n>>", "\n\n>>", "\n"))
      case _ =>
        c.abort(c.enclosingPosition, s"wahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\n${showRaw(c.enclosingImpl)}")
    }

    c.abort(c.enclosingPosition, "\n"+showRaw(c.enclosingImpl))
    */
    //Apply(Ident(TypeName("Class")), List(i.tree))
    
    f
  }
}