package example.service

case class Order(amount: Int, val status: String)
sealed trait OrderMsg
object OrderMsg {
  case class UpdateStatus(status: String) extends OrderMsg
  case class Delete(id: String) extends OrderMsg
}

import example.service.OrderMsg._
class OrderBus {
  def process(msg: OrderMsg): Modification[Order] = msg match {
    case UpdateStatus(status) => order => Updated(order.copy(status = status))
    case Delete(_)            => order => Deleted
  }
}
