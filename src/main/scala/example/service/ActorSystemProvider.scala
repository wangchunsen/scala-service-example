package example.service

import akka.actor.ActorSystem
import Util._
import scala.concurrent.duration._

class ActorSystemProvider {
  def get(): ActorSystem = {
    val actorSystem = ActorSystem("main")
    actorSystem
  }
}


object ActorSystemProvider {
  def shutDownSync(actorSystem: ActorSystem): Unit = {
    actorSystem.log.info("Shutting down system")
    actorSystem.terminate().sync(2 minute)
    actorSystem.log.info("Actor system is down")
  }
}
