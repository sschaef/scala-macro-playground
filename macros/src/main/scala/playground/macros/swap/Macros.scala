package playground.macros.swap

import language.experimental.macros
import scala.reflect.macros.Macro

object Macros {

  // http://weblogs.java.net/blog/cayhorstmann/archive/2013/01/14/first-look-scala-macros
  def swap[A](a: A, b: A): Unit = macro Impl.swap[A]
}

trait Impl extends Macro {

  import c.universe._

  def swap[A](a: c.Expr[A], b: c.Expr[A]): c.Expr[Unit] = {
    c.Expr(Block(
      List(
        ValDef(NoMods, TermName("$temp"), TypeTree(), a.tree),
        assign(a, b),
        assign(b, c.Expr(Ident(TermName("$temp"))))),
      c.literalUnit.tree))
  }

  def assign[A](a: c.Expr[A], b: c.Expr[A]) = a.tree match {
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