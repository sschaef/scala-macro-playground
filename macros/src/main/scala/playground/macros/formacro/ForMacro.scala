package playground.macros.formacro

import language.experimental.macros
import scala.reflect.macros.Context

case class MacroClass(c: Int)

object Macros {
  def forM(header: _)(body: _) = macro __forM

  def __forM(c: Context)(header: c.Tree)(body: c.Tree): c.Tree = {
    import c.universe._
    header match {
      case Block(
        List(
          Assign(Ident(TermName(name)), Literal(Constant(start))),
          Apply(Select(Ident(TermName(name2)), TermName(comparison)), List(Literal(Constant(end))))
        ),
        Apply(Select(Ident(TermName(name3)), TermName(incrementation)), List(Literal(Constant(inc))))
      ) =>
      c.abort(c.enclosingPosition, "it works")
    }
    Literal(Constant(()))
  }


  //type untyped(p: _) = macro untyped_impl

  //def untyped_impl(c: Context)(p: c.Tree): c.Tree = {

  type untyped[A](p: _) = macro untyped_impl[A]

  def untyped_impl[A : c.WeakTypeTag](c: Context)(p: c.Tree): c.Tree = {
    import c.universe._


    p match {
      case Block(
        List(
          ValDef(_, TermName(name), TypeTree(), Literal(Constant(start))),
          Apply(Select(Ident(TermName(name2)), TermName(comparison)), List(Literal(Constant(end))))
        ),
        Apply(Select(Ident(TermName(name3)), TermName(incrementation)), List(Literal(Constant(inc))))
      ) =>
        /*val s = q"""
          var name = $start
          while (true) {}
        """
        c.abort(c.enclosingPosition, s"it works: ${showRaw(s)}")*/
        c.abort(c.enclosingPosition, "it works")
      case _ =>
        c.abort(c.enclosingPosition, showRaw(p))
    }


    val t = Template(
      List(Apply(Ident(typeOf[MacroClass].typeSymbol), List(Literal(Constant(0))))),
      emptyValDef,
      List(DefDef(
        Modifiers(),
        nme.CONSTRUCTOR,
        List(),
        List(List()),
        TypeTree(),
        Block(List(pendingSuperCall), Literal(Constant(())))
    ))
    )
    t
  }
}