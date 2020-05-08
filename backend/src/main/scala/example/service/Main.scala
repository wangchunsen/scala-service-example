package example.service

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import com.typesafe.config.Config
import example.service.Util._
import example.service.repo.RepoModule
import example.service.routes.{BasicRoutes, UserRoute}
import example.service.service.ServiceModule
import example.service.utils.{Arguments, ConfigUtils}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.ref.WeakReference

object Main {
  def main(args: Array[String]): Unit = {
    val arguments_ = Arguments.parse(args)
    val cfg = wireWith(ConfigUtils.loadConfig _)
    val wConfig: WeakReference[Config] = WeakReference(ConfigUtils.loadConfig(arguments_))

    val app = new BasicModule with ServiceModule with RepoModule {
      def config: Config = wConfig()
    }

    val port = wConfig().getOrDefault[Int]("http.port", 8080)

    import app._
    val route: Route = pathPrefix("api") {
      wireWith(UserRoute.apply _)
    } ~
      BasicRoutes.staticResources ~
      BasicRoutes.ping

    implicit val ac = app.unTypedSystem
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    val httpBind = Http().bindAndHandle(route, "0.0.0.0", port = port).unsafeWaitAndGet()

    Runtime.getRuntime.addShutdownHook(new Thread(() => {
      httpBind
        .unbind()
        .flatMap { _ =>
          app.actorSystem.terminate()
          app.actorSystem.whenTerminated
        }(ExecutionContext.global)
        .unsafeWaitAndGet()
    }))
  }
}
