//package example.service
//
//import akka.actor.ActorSystem
//import akka.http.javadsl.server.Rejections
//import akka.http.scaladsl.Http
//import akka.http.scaladsl.Http.ServerBinding
//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix, _}
//import akka.http.scaladsl.server.{Directive, Directive0, PathMatcher0, Route}
//import akka.stream.ActorMaterializer
//import com.softwaremill.macwire._
//import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
//import example.service.Util._
//import example.service.service.{AccountService, JWTService}
//import scaldi.Injectable._
//import scaldi.Injector
//
//import scala.concurrent.ExecutionContextExecutor
//import scala.concurrent.duration._
//
//object Directives {
//  def authDirective(jwtService: JWTService): Directive0 = {
//    import scalaz._
//    import Scalaz._
//    val checkJwt = (token: String) => jwtService.verify(token) >| pass | reject(Rejections.authorizationFailed)
//
//    (parameter("jwt_token") flatMap checkJwt) |
//      (cookie("jwt_token") map (_.value) flatMap checkJwt) |
//      reject(Rejections.authorizationFailed)
//  }
//}
//
//object Actions {
//  def loginAction(accountService: AccountService, jwtService: JWTService): Route = formField('passport, 'password) { (passport, password) =>
////    onSuccess(accountService.accountIdByNameAndPassword(passport, password)) {
////      case Some(id) => complete(jwtService.generate(id.toString, 60 * 10))
////      case None     => complete(StatusCodes.UnprocessableEntity)
////    }
//    ???
//  }
//}
//
//class HttpProvider private (serviceBinding: ServerBinding) {
//  def shutdownSync(): Unit = serviceBinding.unbind().waitAndGet
//}
//
//object HttpProvider {
//  def startup(implicit injector: Injector): HttpProvider = {
//    val port = inject[Int]("http.port")
//    val accountService = inject[AccountService]
//    implicit val system = inject[ActorSystem]
//    val jwtService = inject[JWTService]
//
//    val statics: Route = get {
//      getFromDirectory("/Users/cswang/projects/js/vue/hello-world/dist")
//    }
//
//    import PlayJsonSupport._
//    val route: Route =
//      pathPrefix("api") {
//        (post & path("login")) {
//          wireWith(Actions.loginAction _)
//        } ~
//          Directives.authDirective(jwtService) {
//            pathPrefix("users") {
//              (pathEnd & get) {
//                complete("adsfad")
//              } ~
//                (put & path(Segment)) { name =>
//                  complete("asdfadsf")
//                }
//            }
//          }
//      } ~ statics
//
//    implicit val materializer: ActorMaterializer = ActorMaterializer()
//    // needed for the future flatMap/onComplete in the end
//    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
//
//    val rest = Http().bindAndHandle(route, "localhost", port = port).waitAndGet(200 millisecond)
//    rest.unbind()
//    system.log.info(s"Http service bind to port $port")
//    new HttpProvider(rest)
//  }
//}
