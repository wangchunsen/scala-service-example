package example.service.service

import akka.actor.BootstrapSetup
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

trait CacheMsg

object Clean extends CacheMsg

case class Get(key: String) extends CacheMsg

case class Set(key: String, value: Any) extends CacheMsg

case class Delete(key: String) extends CacheMsg

class Typed {
  val system = ActorSystem(ping(), "demo", BootstrapSetup())

  def ping(values: Map[String, Any] = Map.empty): Behavior[CacheMsg] = Behaviors.receiveMessage[CacheMsg] {
    case Clean => ping(Map.empty)
    case Set(key, value) => ping(values + (key -> value))
    case Delete(key) => ping(values - key)
  }
}
