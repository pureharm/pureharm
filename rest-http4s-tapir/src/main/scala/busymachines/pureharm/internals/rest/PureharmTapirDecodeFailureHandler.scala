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
    * Use [[sttp.tapir.server.ServerDefaults.decodeFailureHandler]] as a reference
    * on how and when to give "no match" responses. Otherwise, upon endpoints will
    * not be walked through to see which one matches.
    *
    * @param badRequestOnPathErrorIfPathShapeMatches
    * from docs of [[sttp.tapir.server.ServerDefaults.FailureHandling#respondWithStatusCode]]
    * "Should a status 400 be returned if the shape of the path
    * of the request matches, but decoding some path segment fails with a [[DecodeResult.Error]]."
    *
    * @param badRequestOnPathInvalidIfPathShapeMatches
    * from docs of [[sttp.tapir.server.ServerDefaults.FailureHandling#respondWithStatusCode]]
    * "Should a status 400 be returned if the shape of the path
    * of the request matches, but decoding some path segment fails with a [[DecodeResult.InvalidValue]]."
    */
  def handler(
    badRequestOnPathErrorIfPathShapeMatches:   Boolean = false,
    badRequestOnPathInvalidIfPathShapeMatches: Boolean = true,
  ): DecodeFailureHandler = { ctx: DecodeFailureContext =>
    ctx.input match {
      case _: EndpointInput.Query[_]             => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointInput.QueryParams[_]       => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointInput.Cookie[_]            => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointIO.Header[_]               => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointIO.Headers[_]              => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointIO.Body[_, _]              => anomalyResponse(StatusCode.BadRequest, ctx)
      case _: EndpointIO.StreamBodyWrapper[_, _] => anomalyResponse(StatusCode.BadRequest, ctx)
      // we assume that the only decode failure that might happen during path segment decoding is an error
      // a non-standard path decoder might return Missing/Multiple/Mismatch, but that would be indistinguishable from
      // a path shape mismatch
      case _: EndpointInput.PathCapture[_]
          if (badRequestOnPathErrorIfPathShapeMatches && ctx.failure.isInstanceOf[DecodeResult.Error]) ||
            (badRequestOnPathInvalidIfPathShapeMatches && ctx.failure.isInstanceOf[DecodeResult.InvalidValue]) =>
        anomalyResponse(StatusCode.BadRequest, ctx)
      // other basic endpoints - the request doesn't match, but not returning a response (trying other endpoints)
      case _: EndpointInput.Basic[_]             => noMatch
      // all other inputs (tuples, mapped) - responding with bad request
      case _ => anomalyResponse(StatusCode.BadRequest, ctx)
    }
  }

  def anomalyResponse(code: StatusCode, ctx: DecodeFailureContext): DecodeFailureHandling = {
    val anomaly: Throwable = ctx.failure match {
      case DecodeResult.Missing                    =>
        println("""
                  |
                  |DecodeResult.Missing    
                  |
                  |""".stripMargin)
        InvalidInputAnomaly(tapirResponse(ctx))
      case DecodeResult.Multiple(vs)               =>
        println(s"""
                   |
                   |DecodeResult.Multiple(
                   |$vs
                   |)
                   |
                   |""".stripMargin)
        InvalidInputAnomaly(tapirResponse(ctx))
      case DecodeResult.Error(original, error)     =>
        println(s"""
                   |
                   |case DecodeResult.Error(
                   |original = $original,
                   |error    = $error
                   |)
                   |
                   |""".stripMargin)
        error match {
          case e: io.circe.DecodingFailure => CirceDecodingAnomaly(e)
          case e: io.circe.ParsingFailure  => CirceParsingAnomaly(e)
          case _ => InvalidInputAnomaly(tapirResponse(ctx))
        }

      case DecodeResult.Mismatch(expected, actual) =>
        println(s"""
                   |
                   |DecodeResult.Mismatch(
                   |expected = $expected,
                   |actual   = $actual
                   |)
                   |
                   |""".stripMargin)
        InvalidInputAnomaly(tapirResponse(ctx))
      case DecodeResult.InvalidValue(errors)       =>
        println(s"""
                   |
                   |DDecodeResult.InvalidValue(
                   |$errors
                   |)
                   |
                   |""".stripMargin)
        InvalidInputAnomaly(tapirResponse(ctx))
    }
    response(jsonBody[Throwable].and(statusCode(code)))(anomaly)
  }

  def tapirResponse(ctx: DecodeFailureContext): String =
    ServerDefaults.FailureMessages.failureMessage(ctx)
}
