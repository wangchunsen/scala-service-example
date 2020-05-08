package example.service

import scaldi.Module

package object service {
  def bindings(): Module ={
    new Module {
//      bind[AccountService] to injected[AccountService]
//      bind[JWTService] to new JWTService(inject[String]("jwt.key"))
    }
  }
}
