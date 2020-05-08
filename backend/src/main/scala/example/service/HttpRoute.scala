package example.service

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.{complete, get, path, pathEnd, pathPrefix, post, put, _}
import akka.http.scaladsl.server.Route
import example.service.service.ServiceModule

class HttpRoute(sm: ServiceModule) {
  def main: Route =
    pathPrefix("api") {
      (post & path("login")) {
        complete(ToResponseMarshallable.apply(sm.authService.doLogin()))
      } ~
        pathPrefix("users") {
          (pathEnd & get) {
            complete("adsfad")
          } ~
            (put & path(Segment)) { name =>
              complete("asdfadsf")
            }
        }
    }
}
