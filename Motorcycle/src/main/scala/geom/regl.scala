package com.jc776.geom.gl
import com.jc776.geom.core._

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobalScope
object global extends js.Object {
  def createREGL(): Regl = js.native
  // & createREGL(args...)
}

@js.native
trait Regl extends js.Object {
  def apply[Props](state: State[Props]): Command[Props] = js.native
  def clear(opts: Clear): Unit = js.native

  def frame(f: js.Function1[Frame,Unit]): js.Dynamic = js.native
  // cancel (ok)
  // texture, buffer, framebuffer - useful
  // read, draw, attributes, etc - less useful
  def prop(name: String): js.Dynamic = js.native
  def context(name: String): js.Dynamic = js.native

  def buffer[T](arg: js.Array[T]): js.Dynamic = js.native
}

@ScalaJSDefined
trait State[Props] extends js.Object {
  def frag: js.UndefOr[String] = js.undefined
  def vert: js.UndefOr[String] = js.undefined
  def attributes: js.UndefOr[js.Any] = js.undefined
  def elements: js.UndefOr[js.Array[Idx3]] = js.undefined
  def uniforms: js.UndefOr[js.Any] = js.undefined
  def count: js.UndefOr[Int] = js.undefined
}

@js.native
trait Command[Props] extends js.Object {
  def apply(args: Props): Unit = js.native
}

@ScalaJSDefined
trait Clear extends js.Object {
  def color: js.UndefOr[js.Tuple4[Double,Double,Double,Double]] = js.undefined
  def depth: js.UndefOr[Double] = js.undefined
}

@js.native
trait Frame extends js.Object {
  def time: Double = js.native
  def viewportWidth: Double = js.native
  def viewportHeight: Double = js.native
}
