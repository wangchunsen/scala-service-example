package example.service

import akka.actor.ActorSystem
import example.service.Util._

import scala.concurrent.duration._

object ActorSystemProvider {

  def shutDownSync(actorSystem: ActorSystem): Unit = {

  }

  def newSystem(): ActorSystem = {
    val actorSystem = ActorSystem("main")
    actorSystem
  }
}
