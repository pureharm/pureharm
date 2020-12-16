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

import busymachines.pureharm.effects.Concurrent
import org.http4s

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
trait PureharmRestHttp4sTypeAliases {
  type HttpApp[F[_]] = http4s.HttpApp[F]
  val HttpApp: http4s.HttpApp.type = http4s.HttpApp

  type HttpRoutes[F[_]] = http4s.HttpRoutes[F]
  val HttpRoutes: http4s.HttpRoutes.type = http4s.HttpRoutes

  type Http4sDsl[F[_]] = http4s.dsl.Http4sDsl[F]
  val Http4sDsl: http4s.dsl.Http4sDsl.type = http4s.dsl.Http4sDsl

  type Http4sRuntime[F[_], EffectType <: Concurrent[F]] =
    busymachines.pureharm.internals.rest.Http4sRuntime[F, EffectType]

  type RestDefs[F[_], ET <: Concurrent[F], RT <: Http4sRuntime[F, ET]] =
    busymachines.pureharm.internals.rest.RestDefs[F, ET, RT]
}
