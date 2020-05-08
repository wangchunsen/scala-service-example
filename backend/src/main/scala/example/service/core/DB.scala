//package example.service.core
//
//import com.typesafe.config.Config
//import example.service.Util._
//import slick.basic.{BasicProfile, DatabaseConfig}
//import slick.jdbc.JdbcProfile
//
//trait Repository extends LifeCycleAware {
//  type P <: JdbcProfile
//  def database: P#Backend#Database
//  def profile: P
//  def tables: Tables
//}
//
//object Repository {
//  def fromConfig(config: Config): Repository = {
//    val (profile_, database_) = {
//      val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("db-default", config = config)
//      val profile = dbConfig.profile
//      val db = dbConfig.db
//
//      profile -> db
//    }
//    type Database = profile_.backend.Database
//
//    val tables_ : Tables = new Tables {
//      override val profile: JdbcProfile = profile_
//    }
//
//    new Repository {
//      override type P = JdbcProfile
//
//      override def database = database_
//
//      override def profile = profile_
//
//      override def tables = tables_
//
//      override def onStart(): Unit = {
//        import profile_.api._
//
//        import scala.concurrent.duration._
//        println("Testing db connections")
//        val result = database_.run(sql"select 1".as[Int])
//        assert((result waitAndGet (3 seconds))(0) == 1)
//        println("Db connection is OK")
//      }
//
//      override def onStop(): Unit = ()
//    }
//  }
//}
//
////final class DB(config: Config) extends Repository {
////  override type P = JdbcProfile
////
////  val (profile, database) = {
////    val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("db-default", config = config)
////    val profile = dbConfig.profile
////    val db = dbConfig.db
////
////    profile -> db
////  }
////  type Database = profile.backend.Database
////
////  val api: profile.API = profile.api
////  val tables: Tables = new Tables {
////    override val profile: JdbcProfile = DB.this.profile
////  }
////
////  def testConnection(): Unit = {
////    import profile.api._
////
////    import scala.concurrent.duration._
////
////    val testResult = database.run(sql"select 1".as[Int])
////    assert((testResult waitAndGet (3 seconds))(0) == 1)
////  }
////
////  @inline
////  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = database.run(a)
////}
