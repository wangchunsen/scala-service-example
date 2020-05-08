package example.service.core

trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  case class AccountRow(id: Long, passport: String, password: String, createAt: java.sql.Timestamp, enabled: Boolean = true)
  implicit def GetResultAccountRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Boolean]): GR[AccountRow] = GR{
    prs => import prs._
      AccountRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table ACCOUNT. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends profile.api.Table[AccountRow](_tableTag, "ACCOUNT") {
    def * = (id, passport, password, createAt, enabled) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(passport), Rep.Some(password), Rep.Some(createAt), Rep.Some(enabled)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column PASSPORT SqlType(VARCHAR), Length(40,true) */
    val passport: Rep[String] = column[String]("PASSPORT", O.Length(40,varying=true))
    /** Database column PASSWORD SqlType(VARCHAR), Length(40,true) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(40,varying=true))
    /** Database column CREATE_AT SqlType(TIMESTAMP) */
    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATE_AT")
    /** Database column ENABLED SqlType(TINYINT), Default(1) */
    val enabled: Rep[Boolean] = column[Boolean]("ENABLED", O.Default(true))
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))
}
