package busymachines.pureharm.rest

import busymachines.pureharm.internals.rest.Http4sRuntime
import cats.effect.Sync

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
trait PureharmRestTypeDefinitions extends PureharmRestHttp4sTypeDefinitions with PureharmRestTapirTypeDefinitions {

  type Http4sRuntime[F[_], EffectType <: Sync[F]] =
    busymachines.pureharm.internals.rest.Http4sRuntime[F, EffectType]

  type RestDefs[F[_], ET <: Sync[F], Runtime <: Http4sRuntime[F, ET]] =
    busymachines.pureharm.internals.rest.RestDefs[F, ET, Runtime]
}
