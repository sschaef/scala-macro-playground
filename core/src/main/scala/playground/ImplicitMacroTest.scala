package playground

import playground.macros.implicittest.Macros._

object ImplicitMacroTest {

  val a = 3
  val b = 5
  
  println(a add b)

  def doIt() = block {
    f1(3) + f2(5)
  }
  {
    def f1(i: Int) = i
    def f2(i: Int) = i
  }

  println(doIt)
}