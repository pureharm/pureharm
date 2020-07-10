package busymachines.pureharm.rest.testkit.temp

import busymachines.pureharm.testkit.PureharmTest

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
class CodeGen extends PureharmTest {

  def data(i: Int): String =
    s"""
       |
       |//---------------------------------
       |final case class MyInputType$i(
       |  f1: PHString,
       |  f2: PHInt,
       |  f3: PHLong,
       |  f4: List[PHUUID],
       |  f5: Option[PHString],
       |)
       |
       |object MyInputType$i {
       |  import busymachines.pureharm.json._
       |  import busymachines.pureharm.json.implicits._
       |
       |  implicit val codec: Codec[MyInputType$i] = derive.codec[MyInputType$i]
       |}
       |
       |final case class MyOutputType$i(
       |  id: PHUUID,
       |  f1: PHString,
       |  f2: PHInt,
       |  f3: PHLong,
       |  f4: List[PHUUID],
       |  f5: Option[PHString],
       |)
       |
       |object MyOutputType$i {
       |  import busymachines.pureharm.json._
       |  import busymachines.pureharm.json.implicits._
       |  implicit val codec: Codec[MyOutputType$i] = derive.codec[MyOutputType$i]
       |}
       |//---------------------------------
       |""".stripMargin

  def tapir(i: Int): String =
    s"""
       |//---------------------------------
       |object TempTapirEndpoints$i {
       |
       |  import busymachines.pureharm.rest._
       |  import busymachines.pureharm.rest.implicits._
       |  import sttp.tapir._
       |  import sttp.tapir.json.circe._
       |
       |  val testPath: String = "test"
       |
       |  val testRouteGet: SimpleEndpoint[PHUUID, MyOutputType$i] =
       |    phEndpoint.get
       |      .in(testPath / path[PHUUID])
       |      .out(jsonBody[MyOutputType$i])
       |
       |  val testRoutePost: SimpleEndpoint[MyInputType$i, MyOutputType$i] =
       |    phEndpoint.post
       |      .in(testPath)
       |      .in(jsonBody[MyInputType$i])
       |      .out(jsonBody[MyOutputType$i])
       |}
       |//---------------------------------
       |""".stripMargin

  import busymachines.pureharm.effects._

  test("data") {
    IO {
      println("""
                |package busymachines.pureharm.rest.temp.stress
                |
                |import busymachines.pureharm.rest.temp._
                |
                |""".stripMargin)
      println(List.range(1, 100).map(data).mkString("\n\n"))
    }.as(succeed)
  }

  test("tapir") {
    IO {
      println("""
                |package busymachines.pureharm.rest.temp.stress
                |
                |import busymachines.pureharm.rest.temp._
                |
                |""".stripMargin)
      println(List.range(1, 100).map(tapir).mkString("\n\n"))
    }.as(succeed)
  }
}
