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
package busymachines.pureharm.rest.temp

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.rest.HttpApp
import busymachines.pureharm.rest.temp.TempTapirEndpoints.{MyAuthStack, SomeAPI, SomeOrganizer, TestHttp4sRuntime}
import org.http4s.HttpRoutes
import org.http4s.server.Router

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jul 2020
  */
object MyAppEcology {

  case class MyApp[F[_]](
    domainLogic: DomainLogic[F],
    restAPIs:    RestAPIs[F],
    http4sApp:   HttpApp[F],
  )

  final case class DomainLogic[F[_]](
    authStack: MyAuthStack[F],
    organizer: SomeOrganizer[F],
  )

  final case class RestAPIs[F[_]](
    someAPI: SomeAPI[F]
  )

  def everything[F[_]: Sync](implicit http4sRuntime: TestHttp4sRuntime[F]): F[MyApp[F]] =
    for {
      domainApps <- MyAppEcology.domainLogic[F]
      domainAPIs <- MyAppEcology.restAPIs[F](domainApps)(http4sRuntime)
      app        <- MyAppEcology.http4sApp[F](domainAPIs)
    } yield MyApp[F](
      domainApps,
      domainAPIs,
      app,
    )

  def domainLogic[F[_]: Sync]: F[DomainLogic[F]] =
    for {
      authStack <- new MyAuthStack[F]().pure[F]
      organizer <- new SomeOrganizer[F]()(Sync[F], authStack).pure[F]
    } yield DomainLogic(
      authStack,
      organizer,
    )

  def restAPIs[F[_]](
    apps:                   DomainLogic[F]
  )(implicit http4sRuntime: TestHttp4sRuntime[F]): F[RestAPIs[F]] = {
    import http4sRuntime.F
    RestAPIs(
      new SomeAPI[F](apps.organizer)
    ).pure[F]
  }

  def http4sApp[F[_]: Monad](apis: MyAppEcology.RestAPIs[F]): F[HttpApp[F]] = {
    import org.http4s.implicits._
    val routes: HttpRoutes[F] = NEList
      .of(
        apis.someAPI.routes
      )
      .reduceK
    Router("/api" -> routes).orNotFound.pure[F]
  }

}
