package example.service.effects

import cats.effect.IO
import com.typesafe.config.Config
import example.service.core.Tables
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

trait HasDB {
  import slick.dbio.{DBIOAction, NoStream}
  val tables: Tables
  def runAction[R](action: DBIOAction[R, NoStream, Nothing]): IO[R]
}
trait HasConfig {
  val config: Config
}
import example.service.Util._
object HasDBImp {
  def apply(config: Config): HasDB = {
    val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("db", config)
    val _tables = new Tables {
      override val profile = dbConfig.profile
    }
    import _tables.profile.api._
    val db = dbConfig.db
    require(
      db.run(sql"select 1".as[Int].head).unsafeWaitAndGet() == 1,
      "Database ping fail"
    )

    new HasDB {
      implicit val ctxshift = IO.contextShift(ExecutionContext.global)
      override val tables: Tables = _tables
      override def runAction[R](action: DBIOAction[R, NoStream, Nothing]) = IO fromFuture IO { db.run(action) }
    }
  }
}
