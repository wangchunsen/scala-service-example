//package example.service
//import akka.actor.typed.ActorSystem
//import example.service.mode.User
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//
//object MongoInit extends App {
//
//  // Use a Connection String
//
////  // or provide custom MongoClientSettings
////  val settings: MongoClientSettings = MongoClientSettings.builder()
////    .applyToClusterSettings(b => b.hosts(List(new ServerAddress("localhost")).asJava))
////    .build()
//
////  val mongoClient: MongoClient = MongoClient(settings)
//  trait UserAction
//
//  object IncVersion extends UserAction
//
//  val userBussiness = new BusinessLayer[User, UserAction] {
//    override val onMsg: UserAction => Modification[User] = {
//      case IncVersion => updateVersion
//    }
//
//    private def updateVersion(user: User): Modify[User] = {
//      Updated(user.copy(id = user.id + 1))
//    }
//  }
//  val repo = MongoRepoImp.getRepo[User]("myapp")
//
//  def rootBehavior() = DomainSupportBehavior.create(userBussiness, repo)
//  private val system = ActorSystem(rootBehavior(), "default")
//
//  private val initi = repo.create(User(id = 3, name = "lalalalal"))
//  private val row = Await.result(initi, Duration.Inf)
//
//  system ! Bussiness(IncVersion)
//  system ! Initialed(row)
//
//  Thread.sleep(5000)
//}
