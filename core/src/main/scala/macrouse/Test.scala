//import macrotest._

package macrouse

object X extends macrotest.Macros.Class(F.value) with App {
  println(">>>>>>>>>>>>>>>>>>>>>START")
  println(c)
  println(implicitlyAddedMethod)
  println(implicitlyAddedMethod2)

  import reflect.runtime.{currentMirror => m}
  println(m.reflect(this).symbol.baseClasses)

  println(">>>>>>>>>>>>>>>>>>>>>EXIT")
}

object F {
  def value = 15
}
/*
object Test extends Macros.TM with App {
  this.hello
}
*/