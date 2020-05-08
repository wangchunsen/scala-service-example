package example.service.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import example.service.service.UserService

object UserRoute {
  def apply(service:UserService): Route =
    pathPrefix("users") {
      (pathEnd & get) {
        complete("adsfad")
      } ~
        (put & path(Segment)) { name =>
          complete("asdfadsf")
        }
    }
}
