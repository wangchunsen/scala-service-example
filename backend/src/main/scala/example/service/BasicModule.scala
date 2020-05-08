package example.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import com.softwaremill.macwire.wireWith
import com.typesafe.config.Config

trait BasicModule {
  def config: Config
  implicit lazy val actorSystem: ActorSystem[Unit] = wireWith(CoreModule.actorSystem _)
  def unTypedSystem: akka.actor.ActorSystem = actorSystem.toClassic
}
