package busymachines.pureharm.internals.rest

import cats.effect.{ContextShift, Sync}
import sttp.tapir.server.http4s.Http4sServerOptions

/**
  */
trait RestDefs[F[_], ET <: Sync[F], Runtime <: Http4sRuntime[F, ET]] {

  protected def http4sRuntime: Http4sRuntime[F, ET]

  implicit def F:            ET              = http4sRuntime.F
  implicit def contextShift: ContextShift[F] = http4sRuntime.contextShift

  implicit def http4Ops: Http4sServerOptions[F] = http4sRuntime.pureharmHTT4sServerOption
}
