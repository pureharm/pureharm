package busymachines.pureharm.rest

import org.http4s

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
trait PureharmRestTypeDefinitions {
  type HttpApp[F[_]]    = http4s.HttpApp[F]
  type HttpRoutes[F[_]] = http4s.HttpRoutes[F]
}
