package playground.macros.swap

import language.experimental.macros
import scala.reflect.macros.Macro

object Macros {

  // http://weblogs.java.net/blog/cayhorstmann/archive/2013/01/14/first-look-scala-macros
  def swap(a: Any, b: Any): Unit = macro Impl.swap
}

trait Impl extends Macro {

  import c.universe._, Flag._

  def swap(a: c.Expr[Any], b: c.Expr[Any]): c.Expr[Unit] = {
    c.Expr(Block(
      List(
        ValDef(Modifiers(MUTABLE), TermName("$temp"), TypeTree(), a.tree),
        assign(a, b),
        assign(b, c.Expr[Any](Ident(TermName("$temp"))))),
      Literal(Constant(()))))
  }

  def assign(a: c.Expr[Any], b: c.Expr[Any]) = a.tree match {
    case ident: Ident =>
      Assign(ident, b.tree)

    case Apply(Select(obj, TermName("apply")), List(index)) =>
      Apply(Select(obj, TermName("update")), List(index, b.tree))

    case Select(obj, TermName(name)) =>
      Apply(Select(obj, TermName(name+"_$eq")), List(b.tree))

    case _ =>
      c.abort(a.tree.pos, "Expected `variable` or `variable(index)`")
  }
}