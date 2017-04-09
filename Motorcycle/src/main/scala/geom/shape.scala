package com.jc776.geom.shape
import com.jc776.geom.core._

import scala.scalajs.js
import collection.mutable
import js.JSConverters._

object `package` {
  def epsilon(n: Double) = n < 0.00001

  def weight(s: Double, r: Double, a: Double) = {
    math.atan2(r, (s - a))
  }

  def angleNormals(
    positions: js.Array[Vec3],
    cells: js.Array[Idx3]
  ) = {
    val normals = positions.map(x => Vec3(0,0,0))
    for(cell <- cells) {
      val a = positions(cell._1)
      val b = positions(cell._2)
      val c = positions(cell._3)

      val ab = (a-b); val abl = ab.length()
      val bc = (b-c); val bcl = bc.length()
      val ca = (c-a); val cal = ca.length()

      if(!epsilon(abl) && !epsilon(bcl) && !epsilon(cal)){
        val s = 0.5 * (abl + bcl + cal)
        val r = math.sqrt((s - abl)*(s - bcl)*(s - cal)/s)
        val n = ab.cross(bc).unit()

        normals(cell._1) = normals(cell._1).madd(weight(s,r,bcl), n)
        normals(cell._2) = normals(cell._2).madd(weight(s,r,cal), n)
        normals(cell._2) = normals(cell._3).madd(weight(s,r,abl), n)
      }
    }
    normals
  }

  val Fpos = js.Array(
    Vec3(0,0,0),
    Vec3(30,0,0),
    Vec3(0,150,0),
    Vec3(0,150,0),
    Vec3(30,0,0),
    Vec3(30,150,0),

    // top rung front
    Vec3(30,0,0),
    Vec3(100,   0,  0),
    Vec3(30,  30,  0),
    Vec3(30,  30,  0),
    Vec3(100,   0,  0),
    Vec3(100,  30,  0),

    // middle rung front
    Vec3(30,  60,  0),
    Vec3(67,  60,  0),
    Vec3(30,  90,  0),
    Vec3(30,  90,  0),
    Vec3(67,  60,  0),
    Vec3(67,  90,  0),

    // left column back
    Vec3(0,   0,  30),
    Vec3(30,   0,  30),
    Vec3(0, 150,  30),
    Vec3(0, 150,  30),
    Vec3(30,   0,  30),
    Vec3(30, 150,  30),

    // top rung back
    Vec3(30,   0,  30),
    Vec3(100,   0,  30),
    Vec3(30,  30,  30),
    Vec3(30,  30,  30),
    Vec3(100,   0,  30),
    Vec3(100,  30,  30),

    // middle rung back
    Vec3(30,  60,  30),
    Vec3(67,  60,  30),
    Vec3(30,  90,  30),
    Vec3(30,  90,  30),
    Vec3(67,  60,  30),
    Vec3(67,  90,  30),

    // top
    Vec3(0,   0,   0),
    Vec3(100,   0,   0),
    Vec3(100,   0,  30),
    Vec3(0,   0,   0),
    Vec3(100,   0,  30),
    Vec3(0,   0,  30),

    // top rung right
    Vec3(100,   0,   0),
    Vec3(100,  30,   0),
    Vec3(100,  30,  30),
    Vec3(100,   0,   0),
    Vec3(100,  30,  30),
    Vec3(100,   0,  30),

    // under top rung
    Vec3(30,   30,   0),
    Vec3(30,   30,  30),
    Vec3(100,  30,  30),
    Vec3(30,   30,   0),
    Vec3(100,  30,  30),
    Vec3(100,  30,   0),

    // between top rung and middle
    Vec3(30,   30,   0),
    Vec3(30,   30,  30),
    Vec3(30,   60,  30),
    Vec3(30,   30,   0),
    Vec3(30,   60,  30),
    Vec3(30,   60,   0),

    // top of middle rung
    Vec3(30,   60,   0),
    Vec3(30,   60,  30),
    Vec3(67,   60,  30),
    Vec3(30,   60,   0),
    Vec3(67,   60,  30),
    Vec3(67,   60,   0),

    // right of middle rung
    Vec3(67,   60,   0),
    Vec3(67,   60,  30),
    Vec3(67,   90,  30),
    Vec3(67,   60,   0),
    Vec3(67,   90,  30),
    Vec3(67,   90,   0),

    // bottom of middle rung.
    Vec3(30,   90,   0),
    Vec3(30,   90,  30),
    Vec3(67,   90,  30),
    Vec3(30,   90,   0),
    Vec3(67,   90,  30),
    Vec3(67,   90,   0),

    // right of bottom
    Vec3(30,   90,   0),
    Vec3(30,   90,  30),
    Vec3(30,  150,  30),
    Vec3(30,   90,   0),
    Vec3(30,  150,  30),
    Vec3(30,  150,   0),

    // bottom
    Vec3(0,   150,   0),
    Vec3(0,   150,  30),
    Vec3(30,  150,  30),
    Vec3(0,   150,   0),
    Vec3(30,  150,  30),
    Vec3(30,  150,   0),

    // left side
    Vec3(0,   0,   0),
    Vec3(0,   0,  30),
    Vec3(0, 150,  30),
    Vec3(0,   0,   0),
    Vec3(0, 150,  30),
    Vec3(0, 150,   0)
  )

}

trait PrimitiveMesh {
  def positions: js.Array[Vec3]
  def cells: js.Array[Idx3]
  def uvs: js.Array[Vec2]
  // vertex vs face?
  def normals: js.Array[Vec3]
}

case class Torus(major: Double, minor: Double) {

  def toMesh() = {
    val _positions = mutable.ListBuffer[Vec3]()
    val _cells = mutable.ListBuffer[Idx3]()
    val _uvs = mutable.ListBuffer[Vec2]()
    val _normals = mutable.ListBuffer[Vec3]()

    val tau = math.Pi * 2

    val minorSegments = 32
    val majorSegments = 64
    for(j <- 0 to minorSegments; i <- 0 to majorSegments) {
      val id = i.asInstanceOf[Double]
      val jd = j.asInstanceOf[Double]
      val u = id / majorSegments * tau
      val v = jd / minorSegments * tau
      val center = Vec3(major*math.cos(u),minor*math.sin(u),0)
      val vertex = Vec3(
        (major + minor*math.cos(v))*math.cos(u),
        (major + minor*math.cos(v))*math.sin(u),
        minor*math.sin(v)
      )
      _positions += vertex
      _normals += (vertex - center).unit()
      _uvs += Vec2(id / majorSegments, jd / majorSegments)

      if(i != 0 && j != 0) {
        val a = (majorSegments + 1) * j + i - 1
        val b = (majorSegments + 1) * (j - 1) + i - 1
        val c = (majorSegments + 1) * (j - 1) + i
        val d = (majorSegments + 1) * j + i

        _cells += Idx3(a, b, d)
        _cells += Idx3(b, c, d)
      }
    }

    new PrimitiveMesh {
      override val positions = _positions.toJSArray
      override val cells = _cells.toJSArray
      override val uvs = _uvs.toJSArray
      override val normals = _normals.toJSArray
    }
  }
}

case class Triangle3(a: Vec3, b: Vec3, c: Vec3) {
  def toMesh() = {

    val norm = (b - a).cross(c - a).unit()

    new PrimitiveMesh {
      override val positions = js.Array(a, b, c)
      override val cells = js.Array((0,1,2))
      override val uvs = js.Array(Vec2(0,0), Vec2(0.5,1), Vec2(1,0)) //whoops
      override val normals = js.Array(norm, norm, norm)
    }
  }
}
