package playground.macros.implicittest

import language.experimental.macros
import scala.reflect.macros.Context
import scala.reflect.macros.Macro

object Macros {
  implicit class Add(val a: Int) {
    def add(b: _) = macro Impl.add
  }

  implicit class Where(expr: _) {
    def where(block: Unit) = macro Impl.where
  }

  def block(expr: _)(b: Unit) = macro Impl.block
}

trait Impl extends Macro {

  def add(b: c.Tree) = {
    import c.universe._
    import scala.reflect.NameTransformer.encode
    val a = Select(c.prefix.tree, TermName("a"))
    Apply(Select(a, TermName(encode("+"))), List(b))
  }

  def where(block: c.Expr[Unit]) = {
    // not working because implicits can not be applied to untyped macros
    ???
  }

  def block(expr: c.Tree)(b: c.Expr[Unit]) = {
    import c.universe._

    val Expr(Block(decl, _)) = b
    Block(decl, expr)
  }
}