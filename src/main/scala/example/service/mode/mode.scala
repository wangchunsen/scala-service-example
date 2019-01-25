package example.service

import play.api.libs.json.{Json, OWrites, Reads}

package object mode {
  implicit val userReader: Reads[User] = Json.reads[User]
  implicit val userWriter: OWrites[User] = Json.writes[User]
}
