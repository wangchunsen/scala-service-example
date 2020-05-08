package example.service.routes

import java.time.Instant

import akka.http.scaladsl.server.Directives.{complete, get, getFromDirectory, pathPrefix}
import akka.http.scaladsl.server.Route

object BasicRoutes {
  def staticResources: Route = get {
    getFromDirectory("/Users/cswang/projects/js/vue/hello-world/dist")
  }

  def ping: Route = (get & pathPrefix("ping")) {
    complete(Instant.now().toString())
  }
}
