package example.service.db

import com.typesafe.config.Config
import slick.basic.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import example.service.Util._

class DB(config:Config) {
  val (profile, database) = {
    val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("db-default", config=config)
    val profile = dbConfig.profile
    val db = dbConfig.db


    profile -> db
  }
  type Database = profile.backend.Database

  val api = profile.api
  val tables: Tables = new Tables {
    override val profile: JdbcProfile =DB.this.profile
  }

  testConnection()

  private def testConnection(): Unit ={
    import profile.api._

    import scala.concurrent.duration._

    val testResult = database.run(sql"select 1".as[Int])
    assert((testResult sync (3 seconds))(0) == 1)
  }

  @inline
  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = database.run(a)
}
