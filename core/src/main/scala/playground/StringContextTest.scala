package playground

import playground.macros.stringcontext.Macros._

object StringContextTest {
  val place: Piece = Place("world")
  val name: Piece = Name("Eric")
  val pieces = s2"""
    Hello $place
    How are you, $name?
  """
  pieces.located foreach println
}