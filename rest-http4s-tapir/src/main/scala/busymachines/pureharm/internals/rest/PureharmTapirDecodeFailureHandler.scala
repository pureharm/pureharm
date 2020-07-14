package busymachines.pureharm.internals.rest

import busymachines.pureharm.anomaly.InvalidInputAnomaly
import sttp.model.StatusCode
import sttp.tapir.server.DecodeFailureHandling._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jul 2020
  */
object PureharmTapirDecodeFailureHandler {
  import sttp.tapir._
  import sttp.tapir.server._
  import sttp.tapir.json.circe._

  import PureharmTapirEndpoint.thrCC
  implicit val sc: Schema[Throwable] = PureharmTapirSchemas.tapirSchemaAnomalies

  /**
    * Used for reporting error messages when
    *
    * Note for devs:
    * Use [[ServerDefaults.decodeFailureHandler]] as a reference on how and when
    * to give "no match" responses. Otherwise, upon endpoints will not be
    * walked through to see which one matches.
    *
    * @return
    */
  def handler: DecodeFailureHandler = { context =>
    val throwable: Throwable = context.failure match {
      case DecodeResult.Missing                    =>
        println("""
                  |
                  |DecodeResult.Missing    
                  |
                  |""".stripMargin)
        InvalidInputAnomaly("1")
      case DecodeResult.Multiple(vs)               =>
        println("""
                  |
                  |DecodeResult.Multiple(vs)    
                  |
                  |""".stripMargin)
        InvalidInputAnomaly("2")
      case DecodeResult.Error(original, error)     =>
        println("""
                  |
                  |DecodeResult.Multiple(vs)    
                  |
                  |""".stripMargin)
        InvalidInputAnomaly("3")

      case DecodeResult.Mismatch(expected, actual) =>
        println("""
                  |
                  |DecodeResult.Mismatch(expected, actual) 
                  |
                  |""".stripMargin)
        InvalidInputAnomaly("4")
      case DecodeResult.InvalidValue(errors)       =>
        println("""
                  |
                  |DDecodeResult.InvalidValue(errors)   
                  |
                  |""".stripMargin)
        InvalidInputAnomaly("5")
    }

    response(jsonBody[Throwable].and(statusCode(StatusCode.BadRequest)))(throwable)
  }
}
