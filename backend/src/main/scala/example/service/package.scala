package example

package object service {
  sealed trait Modify[+T]
  case class Updated[T](value: T) extends Modify[T]
  object Deleted extends Modify[Nothing]
  object NotChanged extends Modify[Nothing]

  type Modification[T] = T => Modify[T]

  implicit class FlowUtil[T](val obj: T) extends AnyVal {
    def also(fun: T => Unit): T = {
      fun(obj)
      obj
    }
  }
}
