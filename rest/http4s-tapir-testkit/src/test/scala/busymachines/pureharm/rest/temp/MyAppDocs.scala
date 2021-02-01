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

import busymachines.pureharm.rest._
import busymachines.pureharm.effects._
import sttp.tapir.openapi._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jul 2020
  */
object MyAppDocs {

  def printYAML[F[_]: Sync](rapi: MyAppEcology.RestAPIs[F]): F[Unit] = {
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

  def generateYAML[F[_]: Monad](rapi: MyAppEcology.RestAPIs[F]): String = {
    import sttp.tapir.openapi.circe.yaml._
    generate[F](rapi).toYaml
  }

  def generate[F[_]: Monad](rapi: MyAppEcology.RestAPIs[F]): OpenAPI = {
    import sttp.tapir.openapi._
    import sttp.tapir.docs.openapi._

    OpenAPIDocsInterpreter
      .toOpenAPI(
        List[Endpoint[_, _, _, _]](
          rapi.someAPI.testGetEndpoint,
          rapi.someAPI.testPostEndpoint,
        ),
        title   = "MyAppEndpoints",
        version = "1.0.0",
      )
      .servers(List(Server("http://localhost:12345/api").description("localhost server")))

  }
}
