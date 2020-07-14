package busymachines.pureharm.rest.temp

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.rest.HttpApp
import busymachines.pureharm.rest.temp.TempTapirEndpoints.{MyAuthStack, SomeAPI, SomeOrganizer, TestHttp4sRuntime}
import org.http4s.HttpRoutes
import org.http4s.server.Router

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jul 2020
  */
object MyAppWiring {

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
      domainApps <- MyAppWiring.domainLogic[F]
      domainAPIs <- MyAppWiring.restAPIs[F](domainApps)(http4sRuntime)
      app        <- MyAppWiring.http4sApp[F](domainAPIs)
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

  def http4sApp[F[_]: Monad](apis: MyAppWiring.RestAPIs[F]): F[HttpApp[F]] = {
    import org.http4s.implicits._
    val routes: HttpRoutes[F] = NEList
      .of(
        apis.someAPI.routes
      )
      .reduceK
    Router("/api" -> routes).orNotFound.pure[F]
  }

}
