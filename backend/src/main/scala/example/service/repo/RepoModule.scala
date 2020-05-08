package example.service.repo

import com.softwaremill.macwire._
import example.service.BasicModule
import example.service.effects.{HasDB, HasDBImp}
trait RepoModule extends BasicModule {
  lazy val userRepo: UserRepo = wire[UserRepo]
  lazy val db:HasDB = wireWith(HasDBImp.apply _)
//  lazy val repository: Repository = wireWith(Repository.fromConfig _)
}
