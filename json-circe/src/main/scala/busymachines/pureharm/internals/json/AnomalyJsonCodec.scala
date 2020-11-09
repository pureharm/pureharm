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
package busymachines.pureharm.internals.json

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  */
import busymachines.pureharm.anomaly._
import cats.implicits._
import io.circe.Decoder.Result
import io.circe._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
object AnomalyJsonCodec extends AnomalyJsonCodec

trait AnomalyJsonCodec {

  implicit final private val AnomalyIDCodec: Codec[AnomalyID] = new Codec[AnomalyID] {

    override def apply(c: HCursor): Result[AnomalyID] =
      c.as[String].map(AnomalyID.apply)

    override def apply(a: AnomalyID): Json = Json.fromString(a.name)
  }

  implicit final private val pureharmStringOrSeqEncoder: Encoder[Anomaly.Parameter] = {
    case StringWrapper(s)      => Json.fromString(s)
    case SeqStringWrapper(ses) => Json.fromValues(ses.map(Json.fromString))
  }

  implicit final private val pureharmStringOrSeqDecoder: Decoder[Anomaly.Parameter] = (c: HCursor) => {
    val sa: Result[String] = c.as[String]
    if (sa.isRight) {
      sa.map(Anomaly.Parameter)
    }
    else {
      c.as[List[String]].map(Anomaly.Parameter)
    }
  }

  implicit final private val pureharmAnomalyBaseParamsEncoder: Encoder[Anomaly.Parameters] =
    (a: Anomaly.Parameters) =>
      if (a.isEmpty) {
        Json.fromJsonObject(JsonObject.empty)
      }
      else {
        val parametersJson = a.map { p: (String, Anomaly.Parameter) => (p._1, pureharmStringOrSeqEncoder(p._2)) }
        io.circe.Json.fromFields(parametersJson)
      }

  implicit final private val pureharmAnomalyBaseParamsDecoder: Decoder[Anomaly.Parameters] =
    (c: HCursor) => {
      val jsonObj = c.as[JsonObject]
      val m       = jsonObj.map(_.toMap)
      val m2: Either[DecodingFailure, Either[DecodingFailure, Anomaly.Parameters]] = m.map { e: Map[String, Json] =>
        val potentialFailures = e.map { p: (String, Json) => p._2.as[Anomaly.Parameter].map(s => (p._1, s)) }.toList

        if (potentialFailures.nonEmpty) {
          val first: Either[DecodingFailure, List[(String, Anomaly.Parameter)]] =
            potentialFailures.head.map(e => List(e))
          val rest = potentialFailures.tail
          val r: Either[DecodingFailure, List[(String, Anomaly.Parameter)]] = rest.foldRight(first) { (v, acc) =>
            for {
              prevAcc <- acc
              newVal  <- v
            } yield prevAcc :+ newVal
          }
          r.map(l => Anomaly.Parameters(l: _*))
        }
        else {
          Right[DecodingFailure, Anomaly.Parameters](Anomaly.Parameters.empty)
        }
      }
      m2.flatMap(x => identity(x))
    }

  private val nonRecursiveAnomalyBaseDecoder: Decoder[AnomalyBase] = (c: HCursor) =>
    for {
      id     <- c.get[AnomalyID](PureharmJsonConstants.id)
      msg    <- c.get[String](PureharmJsonConstants.message)
      params <- c.getOrElse[Anomaly.Parameters](PureharmJsonConstants.parameters)(Anomaly.Parameters.empty)
    } yield Anomaly(id, msg, params)

  private val nonRecursiveAnomalyBaseEncoder: Encoder[AnomalyBase] = (a: AnomalyBase) => {
    val id      = AnomalyIDCodec(a.id)
    val message = Json.fromString(a.message)
    if (a.parameters.isEmpty) {
      Json.obj(
        PureharmJsonConstants.id      -> id,
        PureharmJsonConstants.message -> message,
      )
    }
    else {
      val params = pureharmAnomalyBaseParamsEncoder(a.parameters)
      Json.obj(
        PureharmJsonConstants.id         -> id,
        PureharmJsonConstants.message    -> message,
        PureharmJsonConstants.parameters -> params,
      )
    }
  }

  implicit final val pureharmAnomalyBaseEncoder: Encoder[AnomalyBase] = {
    case a @ (as: AnomaliesBase) =>
      val fm          = nonRecursiveAnomalyBaseEncoder(a)
      val arr         = as.messages.map(nonRecursiveAnomalyBaseEncoder.apply)
      val messagesObj = Json.obj(PureharmJsonConstants.messages -> Json.arr(arr: _*))
      messagesObj.deepMerge(fm)
    case a => nonRecursiveAnomalyBaseEncoder(a)
  }

  implicit final val pureharmAnomalyBaseDecoder: Decoder[AnomalyBase] = (c: HCursor) => {
    val seqCodec = Decoder.decodeSeq[AnomalyBase](nonRecursiveAnomalyBaseDecoder)
    for {
      fm <- c.as[AnomalyBase](nonRecursiveAnomalyBaseDecoder)
      msgCur = c.downField(PureharmJsonConstants.messages)
      msgs   <-
        if (msgCur.succeeded) {
          c.get[Seq[AnomalyBase]](PureharmJsonConstants.messages)(seqCodec)
        }
        else {
          Seq.empty[AnomalyBase].pure[Result]
        }
      result <-
        if (msgCur.failed) {
          if (msgs.isEmpty) {
            fm.pure[Result]
          }
          else {
            Left(DecodingFailure("This should never happen, how is my sequence not empty?", c.history))
          }
        }
        else {
          if (msgs.isEmpty) {
            Left(DecodingFailure("Anomalies.message needs to be non empty array", c.history))
          }
          else {
            Anomalies(fm.id, fm.message, Anomaly(msgs.head), msgs.tail.map(a => Anomaly(a)): _*).pure[Result]
          }
        }
    } yield result
  }

  //convenience, for explicit use in other pureharm modules
  final val pureharmAnomalyBaseCodec: Codec[AnomalyBase] = Codec.from[AnomalyBase](
    pureharmAnomalyBaseDecoder,
    pureharmAnomalyBaseEncoder,
  )

  implicit final val pureharmThrowableDecoder: Decoder[Throwable] = pureharmAnomalyBaseDecoder.map {
    case e:   Anomaly     => e: Throwable
    case e:   Anomalies   => e: Throwable
    case e:   Catastrophe => e: Throwable
    case thr: Throwable   =>
      UnhandledCatastrophe(s"Unhandled Throwable. This usually signals a bug. ", thr): Throwable
  }

  implicit final val pureharmThrowableEncoder: Encoder[Throwable] = pureharmAnomalyBaseEncoder.contramap {
    case e: AnomalyBase => e: AnomalyBase
    case e: Throwable   =>
      UnhandledCatastrophe(
        s"Unhandled Throwable. This usually signals a bug. ${e.toString}",
        e,
      ): AnomalyBase
  }

  //convenience, for explicit use in other pureharm modules
  final val pureharmThrowableCodec: Codec[Throwable] = Codec.from[Throwable](
    pureharmThrowableDecoder,
    pureharmThrowableEncoder,
  )

}
