package main

import org.flywaydb.core.Flyway
import slick.basic.DatabaseConfig
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.jdbc.{DataSourceJdbcDataSource, DriverDataSource, JdbcProfile}

object Main {
  def main(args: Array[String]): Unit = {
    val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("db-default")

    val flyway = dbConfig.db.source match {
      case d: DataSourceJdbcDataSource =>
        Flyway.configure().dataSource(d.ds).load()
      case d: DriverDataSource =>
        Flyway.configure().dataSource(d).load()
      case d: HikariCPJdbcDataSource =>
        Flyway.configure().dataSource(d.ds).load()
    }

    val command =  args.headOption.get
    val method = flyway.getClass.getDeclaredMethod(command)
    method.invoke(method, flyway)
    flyway.migrate()
    flyway.info().current()
//    orElse Option("migration") get match {
//      case "migration" => flyway.migrate()
//      case "clean" => flyway.clean()
//      case "repair" => flyway.repair()
//      case "info" => printlnMigInfo(flyway)
//    }

  }

  def printMigInfo(flyway: Flyway): Unit = {
    val info = flyway.info()
    List("Current" -> Option(info.current()).toArray, "Applied" -> info.applied(), "Pending" -> info.pending()) foreach { t2 =>
      println(t2._1)
      t2._2.foreach(mi => println(s"${mi.getInstalledOn} ${mi.getType}:  ${mi.getDescription}"))
      println("===========================\n")
    }
  }
}
