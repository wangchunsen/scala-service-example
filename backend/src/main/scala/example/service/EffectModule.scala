package example.service

import example.service.core.Tables
import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.Future
import com.softwaremill.macwire.wire

trait EffectModule extends BasicModule {
  trait HasDB {
    val tables: Tables
    def runAction[R](action: DBIOAction[R, NoStream, Nothing]): Future[R]
  }

}
