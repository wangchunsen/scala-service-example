package example.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}
import example.service.Util._

object CoreModule {
  def actorSystem(config: Config): ActorSystem[Unit] = {
    val systemName = config.getOrDefault[String]("actor.system.name", "default-system")
    ActorSystem(rootBehavior(), systemName)
  }

  private def rootBehavior(): Behavior[Unit] = Behaviors.setup { context =>
    Behaviors.receiveMessage {
      case msg =>
        context.log.info("{}, Receive msg {}", context.self, msg)
        Behaviors.same
    }
  }
}
