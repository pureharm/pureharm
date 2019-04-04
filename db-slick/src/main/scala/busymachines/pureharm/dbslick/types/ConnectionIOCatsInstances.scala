package busymachines.pureharm.dbslick.types

import cats.MonadError

import busymachines.pureharm.dbslick.ConnectionIO
import busymachines.pureharm.dbslick.impl.ConnectionIOMonadError

import scala.concurrent.ExecutionContext

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait ConnectionIOCatsInstances {
  implicit def connectionIOInstance(implicit ec: ExecutionContext): MonadError[ConnectionIO, Throwable] =
    new ConnectionIOMonadError
}
