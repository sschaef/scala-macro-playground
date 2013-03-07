package playground

import playground.macros.introducemember.Macros._

class C

object IntroduceMemberTest {
  addMethod(classOf[C], "foo", (x: Int) => x + 2)
  println(new C().foo(2))
}