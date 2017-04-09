package com.jc776.geom.core

import scala.scalajs.js

object `package` {
  type Vec2 = js.Tuple2[Double, Double]
  object Vec2 {
    def apply(x: Double, y: Double): Vec2 =
      js.Tuple2(x,y)
  }
  implicit class Vec2Ops(val data: Vec2) extends AnyVal {
    def x = data._1
    def y = data._2
  }


  type Vec3 = js.Tuple3[Double, Double, Double]
  object Vec3 {
    def apply(x: Double, y: Double, z: Double): Vec3 =
      js.Tuple3(x,y,z)
  }
  implicit class Vec3Ops(val data: Vec3) extends AnyVal {
    def x = data._1
    def y = data._2
    def z = data._3

    def -(other: Vec3) = Vec3(
      x - other.x,
      y - other.y,
      z - other.z
    )
    def +(other: Vec3) = Vec3(
      x + other.x,
      y + other.y,
      z + other.z
    )
    def madd(m: Double, other: Vec3) = Vec3(
      x + m*other.x,
      y + m*other.y,
      z + m*other.z
    )

    def cross(other: Vec3) = Vec3(
      y*other.z - z*other.y,
      z*other.x - x*other.z,
      x*other.y - y*other.x
    )

    def length2() = x*x + y*y + z*z
    def length() = math.sqrt(length2)

    def unit() = {
      val len = length()
      Vec3(x/len,y/len,z/len)
    }
  }


  type Vec4 = js.Tuple4[Double, Double, Double, Double]
  object Vec4 {
    def apply(x: Double, y: Double, z: Double, w: Double): Vec4 =
      js.Tuple4(x,y,z,w)
  }
  implicit class Vec4Ops(val data: Vec4) extends AnyVal {
    def x = data._1
    def y = data._2
    def z = data._3
    def w = data._4
  }


  type Mat4 = js.Tuple16[
    Double,Double,Double,Double,
    Double,Double,Double,Double,
    Double,Double,Double,Double,
    Double,Double,Double,Double
  ]
  object Mat4 {
    // COLUMN-major, for GL.

    val identity: Mat4 = js.Tuple16(
      1.0,0.0,0.0,0.0,
      0.0,1.0,0.0,0.0,
      0.0,0.0,1.0,0.0,
      0.0,0.0,0.0,1.0
    )

    def perspective(fovy: Double, aspect: Double, near: Double, far: Double): Mat4 = {
      val f = math.tan(math.Pi * 0.5 - 0.5 * fovy)
      val nf = 1.0 / (near - far)
      js.Tuple16(
        f/aspect,0.0,0.0,0.0,
        0.0,f,0.0,0.0,
        0.0,0.0,(far+near)*nf,-1.0,
        0.0,0.0,far*near*nf,0.0
      )
    }
  }
  implicit class Mat4Ops(val data: Mat4) extends AnyVal {
    def translate(v: Vec3): Mat4 = js.Tuple16(
      data._1,data._2,data._3,data._4,
      data._5,data._6,data._7,data._8,
      data._9,data._10,data._11,data._12,
      (data._1 * v.x + data._5 * v.y + data._9 * v.z + data._13),
      (data._2 * v.x + data._6 * v.y + data._10 * v.z + data._14),
      (data._3 * v.x + data._7 * v.y + data._11 * v.z + data._15),
      (data._4 * v.x + data._8 * v.y + data._12 * v.z + data._16)
    )
  }

  type Idx3 = js.Tuple3[Int, Int, Int]
  object Idx3 {
    def apply(a: Int, b: Int, c: Int): Idx3 = js.Tuple3(a,b,c)
  }
}