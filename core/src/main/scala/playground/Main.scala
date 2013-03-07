package playground

object Test extends App {

  def test(title: String)(f: => Any) {
    println
    println("/====================================\\")
    println(s"| $title")
    println("|====================================|")
    f
    println("\\====================================/")
  }

  test("TypedMacroTest")(TypedMacroTest)
  test("ForMacro")(ForMacro)
  test("IntroduceMemberTest")(IntroduceMemberTest)
  test("ImplicitMacroTest")(ImplicitMacroTest)
}