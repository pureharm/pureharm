package busymachines.pureharm.dbslick.types

import cats.MonadError
import busymachines.pureharm.dbslick.{ConnectionIO, ConnectionIOEC}
import busymachines.pureharm.dbslick.impl.ConnectionIOMonadError

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait ConnectionIOCatsInstances {
  implicit def connectionIOInstance(implicit ec: ConnectionIOEC): MonadError[ConnectionIO, Throwable] =
    new ConnectionIOMonadError
}
