/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.rest

import cats.effect.{ContextShift, Sync}
import sttp.tapir.server.http4s.Http4sServerOptions

/**
  */
trait RestDefs[F[_], ET <: Sync[F], RT <: Http4sRuntime[F, ET]] {

  protected def http4sRuntime: RT

  implicit def F:            ET              = http4sRuntime.F
  implicit def contextShift: ContextShift[F] = http4sRuntime.contextShift

  implicit def tapirHttp4Ops: Http4sServerOptions[F] = http4sRuntime.http4sServerOptions
}
