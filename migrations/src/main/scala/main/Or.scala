package main

import scala.annotation.implicitAmbiguous

object Or {
  type |[A, B] = Either[A, B]

  // Evidence that V can convert to Or[A,B]
  trait Evidence[V, A, B] {
    def toOr(v: V): A | B
  }

  object Evidence {
    implicit def ofRight[A, B]: Evidence[B, A, B] = v => Right(v)

    implicit def ofLeft[V, A, B]: Evidence[A, A, B] = v => Left(v)

    implicit def ofLeftRec[V, A1, B1, B](implicit orEvidence: Evidence[V, A1, B1]): Evidence[V, A1 | B1, B] = v => orEvidence.toOr(v)
  }

  implicit def toOr[V, A, B](v: V)(implicit orEvidence: Evidence[V, A, B]): A | B = orEvidence.toOr(v)
}

object Test {
  import Or._
  def tabc(adf: CharSequence | String): String | Int | Boolean = ???

  def sss(): Unit = {
    val vv = Test.tabc(Or.toOr("123"))
  }
}
