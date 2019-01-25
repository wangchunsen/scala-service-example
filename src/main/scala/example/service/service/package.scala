package example.service

import scaldi.Module

package object service {
  def bindings(): Module ={
    new Module {
      bind[UserService] to new UserService()
    }
  }
}
