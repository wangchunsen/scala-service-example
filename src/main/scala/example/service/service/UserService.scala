package example.service.service

import scaldi.{Injectable, Injector}
import Injectable._
import example.service.db.DB
import example.service.{mode => m}
import scala.concurrent.Future

class UserService(implicit injector: Injector) {
  private val db = inject[DB]

  import db.api._
  import db.tables.user
  private val insert = user returning user.map(_.id) into ((mUser, id) => mUser.copy(id = id))

  def allUsers(): Future[Seq[m.User]] = db.run(user.result)

  def addUser(name: String): Future[m.User] = db.run(insert += m.User(0, name))
}
