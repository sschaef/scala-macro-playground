package playground.macros.implicittest

import language.experimental.macros
import scala.reflect.macros.Context
import scala.reflect.macros.Macro

object Macros {
  implicit class C(val a: Int) {
    def add(b: _) = macro Impl.add
  }
}

trait Impl extends Macro {
  def add(b: c.Tree) = {
    import c.universe._
    val s = Select(c.prefix.tree, TermName("a"))
    val res = b match {
      case Ident(tn) =>
        /*
        val value1 = Select(c.prefix.tree, TermName("a"))
        val value2 = Select(c.enclosingImpl, tn)
        Apply(Select(value1, TermName("$plus")), List(value2))
        */
        b
      case _ =>
        c.abort(c.enclosingPosition, "\n>>>\n"+showRaw(b)+"\n>>>\n"+s.toString)
    }
    res
  }
}

/*object X {
  implicit class Where(exp: _) {
    def where(block: Unit) = macro Impl.where_impl
  }
}*/

/*trait Impl extends Macro {

  def where_impl(exp: c.Tree)(block: c.Expr[Unit]) = {
    import c.universe._

    val Expr(Block((inner, _))) = block
    val n = inner :+ exp
    Block(n: _*)
  }
}*/