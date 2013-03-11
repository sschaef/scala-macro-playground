package playground

import playground.macros.swap.Macros._

object SwapTest {

  var a = 5
  var b = 20

  println((a,b))
  swap(a, b)
  println((a,b))

  var arr = Array(1, 2, 3, 4, 5)
  println(arr.deep)
  swap(arr(0), arr(2))
  println(arr.deep)
}