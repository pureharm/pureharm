package busymachines.pureharm.internals.rest

import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.anomaly.{Anomaly, InvalidInputAnomaly, UnauthorizedAnomaly}
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
    * @param missingOrInvalidAuth
    *   This function is used to detect if your flavor of authentication is present or not
    *   and ideally would return a 401 Unauthorized, otherwise, we can return only BadRequest.
    *   Which kinda sucks.
    *
    *   There are values for the three out-of-the-box
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
    missingOrInvalidAuth:                      DecodeFailureContext => Option[DecodeFailureHandling] = noAuthHandler,
    badRequestOnPathErrorIfPathShapeMatches:   Boolean = false,
    badRequestOnPathInvalidIfPathShapeMatches: Boolean = true,
  ): DecodeFailureHandler = { ctx: DecodeFailureContext =>
    println(
      s"""
         |
         |ctx.input   = ${ctx.input}
         |ctx.failure = ${ctx.failure}
         |""".stripMargin
    )

    val tapirDefaultFlow: PartialFunction[DecodeFailureContext, DecodeFailureHandling] = {
      case DecodeFailureContext(input, _) =>
        input match {
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

    missingOrInvalidAuth(ctx).getOrElse(tapirDefaultFlow(ctx))
  }

  def noAuthHandler: DecodeFailureContext => Option[DecodeFailureHandling] = _ => Option.empty

  def missingBearerAuth: DecodeFailureContext => Option[DecodeFailureHandling] = _ => Option.empty

  def missingApiKeyAuth: DecodeFailureContext => Option[DecodeFailureHandling] = _ => Option.empty

  def missingCustomHeaderAuth(headerName: String): DecodeFailureContext => Option[DecodeFailureHandling] = { ctx =>
    ctx.input match {
      case h: EndpointIO.Header[_] if h.name.compareToIgnoreCase(headerName) == 0 =>
        ctx.failure match {
          case DecodeResult.Missing =>
            response(jsonBody[Throwable].and(statusCode(StatusCode.Unauthorized)))(MissingAuthHeader(h.name)).some
          case e: DecodeResult.Error =>
            response(jsonBody[Throwable].and(statusCode(StatusCode.Unauthorized)))(
              InvalidAuthHeader(h.name, e.original, e.error.toString)
            ).some
          case _ => Option.empty
        }
      case _ => Option.empty
    }

  }

  def anomalyResponse(code: StatusCode, ctx: DecodeFailureContext): DecodeFailureHandling = {
    __debug(ctx.failure)
    val anomaly: Throwable = ctx.failure match {
      case DecodeResult.Missing                    =>
        ctx.input match {
          case qp: EndpointInput.Query[_] =>
            MissingQueryParam(qp.name, tapirResponse(ctx).replace("Invalid value for", "Missing"))
          case hd: EndpointIO.Header[_]   =>
            MissingHeader(hd.name, tapirResponse(ctx).replace("Invalid value for", "Missing"))
          case in: Product                =>
            MissingRequestPartGeneric(in.productPrefix, tapirResponse(ctx).replace("Invalid value for", "Missing"))
          case _:  EndpointInput[_]       =>
            MissingRequestPartGeneric("[See diagnostic]", tapirResponse(ctx).replace("Invalid value for", "Missing"))
        }
      case DecodeResult.Multiple(vs)               =>
        InvalidMultiple(tapirResponse(ctx))
      case DecodeResult.Error(original, error)     =>
        error match {
          case e: io.circe.DecodingFailure => CirceDecodingAnomaly(e)
          case e: io.circe.ParsingFailure  => CirceParsingAnomaly(e)
          case _ =>
            ctx.input match {
              case qp: EndpointInput.Query[_] =>
                InvalidQueryParam(qp.name, original, tapirResponse(ctx), error.toString)
              case hd: EndpointIO.Header[_]   =>
                InvalidHeader(hd.name, original, tapirResponse(ctx), error.toString)
              case in: Product                =>
                InvalidRequestPartGeneric(in.productPrefix, original, tapirResponse(ctx), error.toString)
              case _:  EndpointInput[_]       =>
                InvalidRequestPartGeneric("[See Diagnostic]", original, tapirResponse(ctx), error.toString)
            }
        }

      case DecodeResult.Mismatch(expected, actual) =>
        ctx.input match {
          case p: Product          => MismatchAnomaly(p.productPrefix, expected, actual, tapirResponse(ctx))
          case _: EndpointInput[_] => MismatchAnomaly("Unknown", expected, actual, tapirResponse(ctx))
        }
      case DecodeResult.InvalidValue(errors)       =>
        ValidationAnomaly(
          tapirResponse(ctx),
          errors.map(_.toString),
        )
    }
    response(jsonBody[Throwable].and(statusCode(code)))(anomaly)
  }

  private def __debug(failure: DecodeResult.Failure): Unit = {
    failure match {
      case DecodeResult.Missing                    =>
        println("""
                  |
                  |DecodeResult.Missing    
                  |
                  |""".stripMargin)

      case DecodeResult.Multiple(vs)               =>
        println(s"""
                   |
                   |DecodeResult.Multiple(
                   |$vs
                   |)
                   |
                   |""".stripMargin)
      case DecodeResult.Error(original, error)     =>
        println(s"""
                   |
                   |case DecodeResult.Error(
                   |original = $original,
                   |error    = $error
                   |)
                   |
                   |""".stripMargin)

      case DecodeResult.Mismatch(expected, actual) =>
        println(s"""
                   |
                   |DecodeResult.Mismatch(
                   |expected = $expected,
                   |actual   = $actual
                   |)
                   |
                   |""".stripMargin)
      case DecodeResult.InvalidValue(errors)       =>
        println(s"""
                   |
                   |DDecodeResult.InvalidValue(
                   |$errors
                   |)
                   |
                   |""".stripMargin)
    }
  }

  def tapirResponse(ctx: DecodeFailureContext): String =
    ServerDefaults.FailureMessages.failureMessage(ctx)
}
