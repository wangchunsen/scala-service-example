package example.service.service

import example.service.repo.UserRepo

class AuthService(userRepo: UserRepo) {
  def doLogin(): String = ???
}
