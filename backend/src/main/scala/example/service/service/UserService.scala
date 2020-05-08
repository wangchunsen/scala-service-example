package example.service.service

import cats.effect.IO
import example.service.core.Tables
import example.service.effects.HasDB

class UserService(db: HasDB) {
  import db.tables.profile.api._
  def getUser(userId: String): IO[Seq[Tables#AccountRow]] = {
    db.runAction(db.tables.Account.filter(_.id === 123L).result)
  }
}
