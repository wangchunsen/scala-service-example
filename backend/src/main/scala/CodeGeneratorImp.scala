//import example.service.repo.RepoModule
//import slick.codegen.{OutputHelpers, SourceCodeGenerator}
//import slick.jdbc.JdbcProfile
//import slick.jdbc.meta.MTable
//import slick.model.Model
//
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, ExecutionContext}
//
//class CodeGeneratorImp(model: Model, version: String) extends SourceCodeGenerator(model) with OutputHelpers {
//  override val ddlEnabled: Boolean = false
//
//  override def code: String = {
//    val superCode = super.code
//    s"""
//    |$superCode
//    |val schemaVersion = $version
//    |""".stripMargin
//  }
//}
//
//object CodeGeneratorImp {
//  private val ignoreTables = Seq("flyway_schema_history")
//
//  def main(args: Array[String]): Unit = {
//    val arguments_ = Arguments.parse(args)
//
//    val repo = new RepoModule {
//      override val arguments: Arguments = arguments_
//    }
//
//    val version = repo.migration.currentVersion()
//    val db = repo.repository.database
//    val profile = repo.repository.profile
//    val outputDir = "./backend/src/main/scala"
//    val pkg = "example.service.db"
//
//    implicit val exeContext = ExecutionContext.global
//    try {
//      val action = MTable.getTables.map(tables => tables.filterNot(table => ignoreTables contains table.name.name.toLowerCase))
//      val getModels = profile.createModel(Some(action), false).withPinnedSession
//      val m = Await.result(db.run(getModels), Duration.Inf)
//      new CodeGeneratorImp(m, version).writeToFile(getProfileName(profile), outputDir, pkg)
//    } finally db.close
//  }
//
//  def getProfileName(profile: JdbcProfile) = profile.getClass.getName.takeWhile(_ != '$')
//}
