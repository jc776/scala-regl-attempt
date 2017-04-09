package com.jc776.geom.color

import scala.scalajs.js

object `package` {
  type RGBA = js.Tuple4[Double,Double,Double,Double]

  object RGBA {
    def apply(r: Double, g: Double, b: Double, a: Double): RGBA =
      js.Tuple4(r,g,b,a)
  }

  implicit class RGBAOps(val data: js.Tuple4[Double,Double,Double,Double]) extends AnyVal {
    def r = data._1
    def g = data._2
    def b = data._3
    def a = data._4
  }
}
