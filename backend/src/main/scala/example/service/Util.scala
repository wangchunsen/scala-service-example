package example.service

import com.typesafe.config.Config

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

object Util {
  implicit class SyncFuture[T](val future: Future[T]) extends AnyVal {
    def unsafeWaitAndGet(duration: Duration = Duration.Inf): T = Await.result(future, duration)
  }

  implicit class ConfigOps(val config: Config) extends AnyVal {
    def getOrDefault[T: ClassTag](path: String, default: => T): T =
      if (config.hasPath(path)) get(path, implicitly[ClassTag[T]])
      else default

    private def get[T](path: String, clz: ClassTag[T]): T =
      (clz match {
        case ClassTag.Int                           => config.getInt(path)
        case c if c.runtimeClass == classOf[String] => config.getString(path)
      }).asInstanceOf[T]
  }
}
