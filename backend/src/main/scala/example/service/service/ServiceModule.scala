package example.service.service

import com.softwaremill.macwire._
import example.service.{BasicModule, CoreModule}
import example.service.repo.RepoModule

trait ServiceModule extends BasicModule { self: RepoModule =>
//  lazy val accountService: AccountService = wire[AccountService]
  lazy val jWTService: JWTService = wire[JWTService]
  lazy val authService: AuthService = wire[AuthService]
  lazy val userService: UserService = wire[UserService]
}
