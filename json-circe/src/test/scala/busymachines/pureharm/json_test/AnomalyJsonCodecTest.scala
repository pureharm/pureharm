package busymachines.pureharm.json_test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
import busymachines.pureharm._
import org.scalatest.flatspec.AnyFlatSpec

/**
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 10 Aug 2017
  *
  */
class AnomalyJsonCodecTest extends AnyFlatSpec {

  import busymachines.pureharm.json_impl._
  import busymachines.pureharm.json.implicits._
  import AnomalyJsonCodec._

  behavior of "... serializing simple Anomalies"

  it should "... encode a NotFoundAnomaly" in {
    val failure: AnomalyBase = NotFoundAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "0",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... encode a UnauthorizedAnomaly" in {
    val failure: AnomalyBase = UnauthorizedAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "1",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... encode a ForbiddenAnomaly" in {
    val failure: AnomalyBase = ForbiddenAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "2",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... encode a DeniedAnomaly" in {
    val failure: AnomalyBase = DeniedAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "3",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... encode a InvalidInputAnomaly" in {
    val failure: AnomalyBase = InvalidInputAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "4",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... encode a ConflictAnomaly" in {
    val failure: AnomalyBase = ConflictAnomaly(
      "test message",
      Anomaly.Parameters(
        "one" -> "one",
        "two" -> List("one", "two"),
      ),
    )

    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """{
          |  "id" : "5",
          |  "message" : "test message",
          |  "parameters" : {
          |    "one" : "one",
          |    "two" : [
          |      "one",
          |      "two"
          |    ]
          |  }
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  behavior of "... serializing composite Anomalies"

  it should "... encode Anomalies" in {
    val failure: AnomaliesBase = Anomalies(
      AnomalyID("test"),
      "test message",
      NotFoundAnomaly(
        "one",
        Anomaly.Parameters(
          "3" -> "1",
          "4" -> List("1", "2"),
        ),
      ),
      NotFoundAnomaly(
        "two",
        Anomaly.Parameters(
          "5" -> "6",
          "6" -> List("6", "7"),
        ),
      ),
    )
    val rawJson = failure.asJson.spaces2
    assert(
      rawJson ==
        """
          |{
          |  "id" : "test",
          |  "message" : "test message",
          |  "messages" : [
          |    {
          |      "id" : "0",
          |      "message" : "one",
          |      "parameters" : {
          |        "3" : "1",
          |        "4" : [
          |          "1",
          |          "2"
          |        ]
          |      }
          |    },
          |    {
          |      "id" : "0",
          |      "message" : "two",
          |      "parameters" : {
          |        "5" : "6",
          |        "6" : [
          |          "6",
          |          "7"
          |        ]
          |      }
          |    }
          |  ]
          |}
          |""".stripMargin.trim,
    )

    val read = rawJson.unsafeDecodeAs[AnomalyBase]
    assert(read.id.name == failure.id.name, "id")
    assert(read.message == failure.message, "message")
    assert(read.parameters == failure.parameters, "parameters")
  }

  it should "... fail when decoding and empty Anomalies" in {
    val rawJson =
      """
        |{
        |  "id" : "test",
        |  "message" : "test message",
        |  "messages" : []
        |}
        |""".stripMargin.trim

    rawJson.decodeAs[AnomaliesBase] match {
      case Left(_)  => //yey!!!
      case Right(_) => fail("should have failed")
    }
  }
}
