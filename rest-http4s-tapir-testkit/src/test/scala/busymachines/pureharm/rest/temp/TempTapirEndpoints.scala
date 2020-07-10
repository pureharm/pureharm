package busymachines.pureharm.rest.temp

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempTapirEndpoints {

  import busymachines.pureharm.rest._
  import busymachines.pureharm.rest.implicits._
  import sttp.tapir._
  import sttp.tapir.json.circe._

  val testPath: String = "test"

  val testRouteGet: SimpleEndpoint[PHUUID, MyOutputType] =
    phEndpoint.get
      .in(testPath / path[PHUUID])
      .out(jsonBody[MyOutputType])

  val testRoutePost: SimpleEndpoint[MyInputType, MyOutputType] =
    phEndpoint.post
      .in(testPath)
      .in(jsonBody[MyInputType])
      .out(jsonBody[MyOutputType])
}
