package example.service.service

import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtJson}
import scalaz._
import Scalaz._

import scala.language.{implicitConversions, postfixOps}

class JWTService() {
  val key = "12312312"
  def generate(jwtId: String, expireInSeconds: Long): String =
    JwtJson.encode(JwtClaim(jwtId = jwtId.some).expiresIn(expireInSeconds), key, JwtAlgorithm.HS256)

  def verify(token: String): Option[String] =
    JwtJson.decode(token, key, Seq(JwtAlgorithm.HS256)).toOption >>= { claim =>
      Monad[Id].ifM(claim.isValid, claim.jwtId, none)
    }
}
