package busymachines.pureharm.internals.rest

import cats.effect.Sync
import org.http4s

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
trait PureharmRestHttp4sTypeAliases {
  type HttpApp[F[_]] = http4s.HttpApp[F]
  val HttpApp: http4s.HttpApp.type = http4s.HttpApp

  type HttpRoutes[F[_]] = http4s.HttpRoutes[F]
  val HttpRoutes: http4s.HttpRoutes.type = http4s.HttpRoutes

  type Http4sDsl[F[_]] = http4s.dsl.Http4sDsl[F]
  val Http4sDsl: http4s.dsl.Http4sDsl.type = http4s.dsl.Http4sDsl

  type Http4sRuntime[F[_], EffectType <: Sync[F]] =
    busymachines.pureharm.internals.rest.Http4sRuntime[F, EffectType]

  type RestDefs[F[_], ET <: Sync[F], RT <: Http4sRuntime[F, ET]] =
    busymachines.pureharm.internals.rest.RestDefs[F, ET, RT]
}
