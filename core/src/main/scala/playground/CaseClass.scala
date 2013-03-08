package playground

import playground.macros.caseclass.Macros._

class Class

object CaseClassTest {
  
  toCaseClass(classOf[Class])

  val c1 = new Class
  val c2 = new Class

  assert(c1.toString == "test")
  assert(c1.productArity == 0)
  assert(!c1.canEqual(""))
  assert(c1.canEqual(c2))

  println("ok")
}