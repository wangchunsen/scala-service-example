//package example.service
//
//import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, StashBuffer}
//import akka.actor.typed.{ActorRef, Behavior, ExtensibleBehavior}
//import example.service.repo.{DBObj, ID, Repo}
//import slick.lifted.Rep
//
//trait BusinessLayer[Model, Msg] {
//  val onMsg: Msg => Modification[Model]
//}
//
//sealed trait BasicCommand[Model, +Msg]
////case class Save[T](model: Model[T]) extends BasicCommand[T, Nothing]
//case class Complete[M](model: DBObj[M, ID]) extends BasicCommand[M, Nothing]
//case object Error extends BasicCommand[Nothing, Nothing]
//case class Bussiness[Msg](msg: Msg) extends BasicCommand[Nothing, Msg]
//case class Query[M](replyTo: ActorRef[M]) extends BasicCommand[M, Nothing]
//case class Initialed[M](model: DBObj[M, ID]) extends BasicCommand[M, Nothing]
//
//object DomainSupportBehavior {
//  def create[Model, Msg](bussiness: BusinessLayer[Model, Msg], repo: Repo[Model]): Behavior[BasicCommand[Model, Msg]] = {
//    type CMD = BasicCommand[Model, Msg]
//
//    def initial(): Behavior[CMD] = Behaviors.withStash(10) { buffer =>
//      Behaviors.receiveMessage {
//        case Initialed(model) =>
//          buffer.unstashAll(active(model))
//
//        case other =>
//          buffer.stash(other)
//          Behaviors.same
//      }
//    }
//
//    def active(model: DBObj[Model, ID]): Behavior[CMD] = Behaviors.receive { (context, msg) =>
//      msg match {
//        case Bussiness(msg) =>
//          val res = bussiness.onMsg(msg)
//
//          res(model.value) match {
//            case NotChanged => Behaviors.same
//            case Updated(v) =>
//              val newModel = model.update(v)
//              context.pipeToSelf(repo.save(newModel)) {
//                case _ => Complete(model)
//              }
//              pending()
//          }
//
//        case Query(sender: ActorRef[Model]) =>
//          sender ! model.value
//          Behaviors.same
//        case other =>
//          throw new IllegalArgumentException(s"Dont know how to process command $other")
//      }
//    }
//
//    def pending(): Behavior[CMD] = Behaviors.withStash(200) { buffer =>
//      Behaviors.receiveMessage {
//        case Complete(model) =>
//          buffer.unstashAll(active(model))
//        case Error =>
//          Behaviors.same
//        case other =>
//          buffer.stash(other)
//          Behaviors.same
//      }
//    }
//
//    initial()
//  }
//}
//
//trait ManagerMsg[M]
//
//case class QueryObj[M](id: ID, replyTo: ActorRef[Option[M]]) extends ManagerMsg[M]
//case class Do[M,Msg](id: ID, msg: Msg) extends ManagerMsg[M]
//
//object DomainManager {
//  def ccc[Model, Msg](bussiness: BusinessLayer[Model, Msg], repo: Repo[Model]): Behavior[ManagerMsg[Model]] = Behaviors.setup { context =>
//    import context.executionContext
//    def createChild(obj: DBObj[Model, ID]): ActorRef[BasicCommand[Model, Msg]] = {
//      val child = context.spawn(DomainSupportBehavior.create(bussiness, repo), obj.id.toString)
//      child ! Initialed(obj)
//      child
//    }
//
//    Behaviors.receiveMessage[ManagerMsg[Model]] {
//      case QueryObj(id, ref) =>
//        context.child(id.toString) match {
//          case Some(childRef) =>
//            val value: ActorRef[BasicCommand[Model, Msg]] = childRef.asInstanceOf[ActorRef[BasicCommand[Model, Msg]]]
//            value.tell(Query(ref))
//            Behaviors.same
//          case _ =>
//            repo.getById(id).foreach { res =>
//              ref ! res.map(_.value)
//            }
//            Behaviors.same
//        }
//
//      case Do(id, msg) =>
//        context.child(id.toString) match {
//          case Some(childRef) =>
//            val value: ActorRef[BasicCommand[Model, Msg]] = childRef.asInstanceOf[ActorRef[BasicCommand[Model, Msg]]]
//            value ! Bussiness[Msg](msg)
//            Behaviors.same
//          case _ =>
//            repo.getById(id).foreach { res =>
//              createChild(res.get) ! Bussiness(msg)
//            }
//            Behaviors.same
//        }
//    }
//  }
//}
