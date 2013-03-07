package playground

import playground.macros.introducemember.Macros

object TypedMacroTest extends Macros.Class(F.value) {
  println(c)
  println(implicitlyAddedMethod)
  println(implicitlyAddedMethod2)

  //getClass.getMethods filterNot (_.getDeclaringClass.getName == "java.lang.Object") foreach println

  /*
  import reflect.runtime.{currentMirror => m}
  println(m.reflect(this).symbol.baseClasses)
  */
}
/*

  generated:

  object TypedMacro extends macrotest.MacroClass with macrotest.MacroTrait with macrotest.MacroTrait2 {
    def <init>(): macrouse.TypedMacro.type = {
      TypedMacro.super.<init>(F.value());
      TypedMacro.this./*MacroTrait$class*/$init$();
      TypedMacro.this./*MacroTrait2$class*/$init$();
      ()
    };
    scala.this.Predef.println(TypedMacro.this.c());
    scala.this.Predef.println(TypedMacro.this.implicitlyAddedMethod());
    scala.this.Predef.println(TypedMacro.this.implicitlyAddedMethod2());
    scala.this.Predef.println(TypedMacro.this.getClass().getMethods());
    <synthetic> private def readResolve(): Object = macrouse.this.TypedMacro
  };

*/

object F {
  def value = 15
}