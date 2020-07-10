package busymachines.pureharm.rest.temp.stress

import busymachines.pureharm.rest.temp._

//---------------------------------
object TempTapirEndpoints1 {

  import busymachines.pureharm.rest._
  import busymachines.pureharm.rest.implicits._
  import sttp.tapir._
  import sttp.tapir.json.circe._

  val testPath: String = "test"

  val testRouteGet: SimpleEndpoint[PHUUID, MyOutputType1] =
    phEndpoint.get
      .in(testPath / path[PHUUID])
      .out(jsonBody[MyOutputType1])

  val testRoutePost: SimpleEndpoint[MyInputType1, MyOutputType1] =
    phEndpoint.post
      .in(testPath)
      .in(jsonBody[MyInputType1])
      .out(jsonBody[MyOutputType1])
}
//---------------------------------
