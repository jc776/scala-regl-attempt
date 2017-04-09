package com.jc776.motorcycle

import com.jc776.geom.core._
import com.jc776.geom.shape._
import com.jc776.geom.color._
import com.jc776.geom.gl

import gl.{global, Clear, State}
//import com.jc776.game._

import scala.scalajs.js
import js.annotation._
import org.scalajs.dom
import scalatags.JsDom.all._

@JSExportTopLevel("Motorcycle")
object Main {


  @ScalaJSDefined
  class PrimitiveU(
    val color: RGBA,
    val light: Vec3,
    val model: Mat4,
    val projection: Mat4
  ) extends js.Object

  @JSExport
  def start(): Unit = {
    testGL()
  }

  def testGL(): Unit = {
    // How can I make this stuff work with "splice"?
    val regl = global.createREGL()

    val mesh = Torus(3,1).toMesh()
    /*
    Triangle3(
      Vec3(-2, -2, 0),
      Vec3(4, -2, 0),
      Vec3(4, 4, 0)
    ).toMesh()*/

    val drawBase = regl(new State[PrimitiveU] {
      override val vert =
        """
          |attribute vec3 position, normal;
          |uniform mat4 projection, model;
          |varying vec3 vposition, vnormal;
          |void main() {
          |  vposition = position;
          |  vnormal = normal;
          |  gl_Position = projection * model * vec4(position, 1);
          |}
        """.stripMargin
      override val frag =
        """
          |precision mediump float;
          |uniform vec4 color;
          |uniform vec3 light;
          |varying vec3 vposition, vnormal;
          |void main() {
          |  vec3 direction = normalize(light - vposition);
          |  vec3 normal = normalize(vnormal);
          |  float power = max(0.0, dot(direction, vnormal));
          |  gl_FragColor = mix(vec4(power,power,power,1.0),color,0.5);
          |}
        """.stripMargin

      override val attributes = js.Dynamic.literal(
        position = regl.buffer(mesh.positions),
        normal = regl.buffer(angleNormals(mesh.positions, mesh.cells))
      )
      override val elements = mesh.cells
      //override val count = 3

      override val uniforms = js.Dynamic.literal(
        projection = regl.prop("projection"),
        model = regl.prop("model"),
        light = regl.prop("light"),
        color = regl.prop("color")
      )
    })

    regl.frame(f => {
      //cc.cancel()
      val time: Double = f.time
      regl.clear(new Clear {
        override val color = js.defined((0.0,0.0,0.0,1.0))
        override val depth = 1
      })

      val psp = Mat4.perspective(
        math.Pi / 6,
        f.viewportWidth/f.viewportHeight,
        10,
        1000
      )

      def us(t: Double, model: Mat4) = new PrimitiveU(
        RGBA(
          (math.sin(t+time/3.0)+1)/2,
          (math.sin(t+time/2.0)+1)/2,
          (math.sin(t+time/5.0)+1)/2,
          1
        ),
        light = Vec3(5,5,0),
        model = model,
        projection = psp
      )

      drawBase(us(0,Mat4.identity.translate(Vec3(3,2,-40))))
      drawBase(us(math.Pi/2,Mat4.identity.translate(Vec3(-3,-2,-40))))
    })
  }
}
