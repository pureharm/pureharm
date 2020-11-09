/** Copyright (c) 2017-2019 BusyMachines
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

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jul 2020
  */
trait PureharmTapirServerAliases {
  type DecodeFailureHandling = sttp.tapir.server.DecodeFailureHandling
  val DecodeFailureHandling: sttp.tapir.server.DecodeFailureHandling.type = sttp.tapir.server.DecodeFailureHandling

  type DecodeFailureContext = sttp.tapir.server.DecodeFailureContext
  val DecodeFailureContext: sttp.tapir.server.DecodeFailureContext.type = sttp.tapir.server.DecodeFailureContext

  type DecodeFailureHandler = DecodeFailureContext => DecodeFailureHandling

  val ServerDefaults: sttp.tapir.server.ServerDefaults.type = sttp.tapir.server.ServerDefaults

  type ServerEndpoint[I, E, O, +S, F[_]]              = sttp.tapir.server.ServerEndpoint[I, E, O, S, F]
  type PartialServerEndpoint[U, I, E, O, +S, F[_]]    = sttp.tapir.server.PartialServerEndpoint[U, I, E, O, S, F]
  type ServerEndpointInParts[U, R, I, E, O, +S, F[_]] = sttp.tapir.server.ServerEndpointInParts[U, R, I, E, O, S, F]

  type SimpleServerEndpoint[I, O, F[_]] = sttp.tapir.server.ServerEndpoint[I, Throwable, O, Nothing, F]

  type Http4sServerOptions[F[_]] = sttp.tapir.server.http4s.Http4sServerOptions[F]

  val Http4sServerOptions: sttp.tapir.server.http4s.Http4sServerOptions.type =
    sttp.tapir.server.http4s.Http4sServerOptions
}
