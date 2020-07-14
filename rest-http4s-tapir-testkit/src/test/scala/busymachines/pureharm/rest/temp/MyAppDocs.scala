package busymachines.pureharm.rest.temp

import busymachines.pureharm.rest._
import busymachines.pureharm.effects._
import sttp.tapir.openapi._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jul 2020
  */
object MyAppDocs {

  def printYAML[F[_]: Sync](rapi: MyAppWiring.RestAPIs[F]): F[Unit] = {
    val yaml = generateYAML[F](rapi)
    Sync[F].delay(
      println(s"""
                 |
                 |
                 |---------------------------
                 |
                 |OPEN API SPEC FOR OUR APP:
                 |
                 |
                 |$yaml
                 |
                 |
                 |---------------------------
                 |""".stripMargin)
    )
  }

  def generateYAML[F[_]: Monad](rapi: MyAppWiring.RestAPIs[F]): String = {
    import sttp.tapir.openapi.circe.yaml._
    generate[F](rapi).toYaml
  }

  def generate[F[_]: Monad](rapi: MyAppWiring.RestAPIs[F]): OpenAPI = {
    import sttp.tapir.openapi._
    import sttp.tapir.docs.openapi._

    List[Endpoint[_, _, _, _]](
      rapi.someAPI.testGetEndpoint,
      rapi.someAPI.testPostEndpoint,
    ).toOpenAPI("MyAppEndpoints", "1.0.0")
      .servers(List(Server("http://localhost:12345/api").description("localhost server")))

  }
}
