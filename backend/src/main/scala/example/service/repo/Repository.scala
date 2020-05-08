package example.service.repo
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DBObj[V, ID](
    version: Long,
    id: ID,
    value: V,
    updatedAt: Date
)

object DBObj {
  implicit class Ops[T, ID](val obj: DBObj[T, ID]) extends AnyVal {
    def update(value: T): DBObj[T, ID] = obj.copy(
      value = value,
      version = obj.version + 1
    )
  }
}

trait Repository[V, ID, Query,F[_]] {
  final protected type Row = DBObj[V, ID]

  def getById(id: ID): F[Option[Row]]
  def save(row: Row): F[Boolean]
  def delete(id: ID): F[Boolean]
  def query(query: Query): F[Seq[Row]]


  def create(value: V): F[Row]
}
