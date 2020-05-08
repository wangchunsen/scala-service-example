package main

import example.service.utils.{Arguments, ConfigUtils}
import javax.sql.DataSource
import org.flywaydb.core.Flyway
import slick.basic.DatabaseConfig
import slick.codegen.{OutputHelpers, SourceCodeGenerator}
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.jdbc.{DataSourceJdbcDataSource, DriverDataSource, H2Profile, JdbcProfile}
import slick.jdbc.meta.MTable
import slick.model.Model

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

class CodeGeneratorImp(model: Model, version: String) extends SourceCodeGenerator(model) with OutputHelpers {
  override val ddlEnabled: Boolean = false

  override def code: String = {
    val superCode = super.code
    s"""
        |$superCode
        |val schemaVersion = $version
        |""".stripMargin
  }

  override type Table = TableDef
  override def entityName: String => String = (dbName: String) => dbName.toCamelCase
//
//  override def tableName: String => String = (dbName: String) => "T" + dbName.toCamelCase
}

object CodeGeneratorImp {
  private val ignoreTables = Seq("flyway_schema_history")

  def main(args: Array[String]): Unit = {
    import slick.jdbc.JdbcBackend._

    val db = Database.forURL(args(0))
//    DatabaseConfig.forURI()
    val arguments = Arguments.parse(args)
    val profile = H2Profile
//    val config = ConfigUtils.loadConfig(arguments)
//    val profile = db.
    val outputDir = "target/generated"
    val pkg = "example.service.db"

    val datasource: DataSource = db.source match {
      case d: DataSourceJdbcDataSource => d.ds
      case d: DriverDataSource         => d
      case d: HikariCPJdbcDataSource   => d.ds
    }
    val flyway = Flyway.configure().dataSource(datasource).load()
    val schemaVersion = flyway.info().current().getVersion.getVersion()

    implicit val exeContext = ExecutionContext.global
    try {
      val action = MTable.getTables.map(tables => tables.filterNot(table => ignoreTables contains table.name.name.toLowerCase))
      val getModels = profile.createModel(Some(action), false).withPinnedSession
      val m = Await.result(db.run(getModels), Duration.Inf)
      new CodeGeneratorImp(m, schemaVersion).writeToFile(dbConfig.profileName, outputDir, pkg)
    } finally db.close
  }
}
