package busymachines.pureharm.internals.rest

import cats.effect.{ContextShift, Sync}

/**
  */
trait RestDefs[F[_], ET <: Sync[F], Runtime <: Http4sRuntime[F, ET]] {

  protected def http4sRuntime: Http4sRuntime[F, ET]

  implicit def F:            ET              = http4sRuntime.F
  implicit def contextShift: ContextShift[F] = http4sRuntime.contextShift

}
