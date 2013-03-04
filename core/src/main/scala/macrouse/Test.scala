package macrouse

object Test extends App {

  def test(title: String)(f: => Any) {
    println
    println("======================================")
    println(title)
    println("======================================")
    f
  }

  test("TypedMacro")(TypedMacro)
  test("TypedMacro2")(TypedMacro2)
  test("ForMacro")(ForMacro)
  test("IntroduceMember")(IntroduceMember)
}

object F {
  def value = 15
}

object TypedMacro extends macrotest.Macros.Class(F.value) {
  println(c)
  println(implicitlyAddedMethod)
  println(implicitlyAddedMethod2)

  //println(getClass.getMethods)

  /*
  import reflect.runtime.{currentMirror => m}
  println(m.reflect(this).symbol.baseClasses)
  */
}

object TypedMacro2 extends macrotest.Macros.TM {
  getClass.getMethods filterNot (_.getDeclaringClass.getName == "java.lang.Object") foreach println
  hello()
  println("ok")
}

object ForMacro {
  /*import macrotest.Macros.untyped
  import macrotest.Macros.forM

  //object o extends untyped({val i = 0; i < 10; i += 1})
  
  forM({i = 0; i < 10; i += 1}) {
    println(i)
  }
  */

  println("ok")
}


class C

object IntroduceMember {
  macrotest.Macros.addMethod(classOf[C], "foo", (x: Int) => x + 2)
  println(new C().foo(2))
}