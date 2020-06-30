/**
  * Copyright (c) 2017-2019 BusyMachines
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

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  */
import busymachines.pureharm.anomaly._
import io.circe.Decoder.Result
import io.circe._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
object AnomalyJsonCodec extends AnomalyJsonCodec

trait AnomalyJsonCodec {

  implicit final private val AnomalyIDCodec: Codec[AnomalyID] = new Codec[AnomalyID] {

    override def apply(c: HCursor): Result[AnomalyID] =
      c.as[String].map(AnomalyID.apply)

    override def apply(a: AnomalyID): Json = Json.fromString(a.name)
  }

  implicit final private val pureharmStringOrSeqCodec: Codec[Anomaly.Parameter] = new Codec[Anomaly.Parameter] {

    override def apply(a: Anomaly.Parameter): Json =
      a match {
        case StringWrapper(s)      => Json.fromString(s)
        case SeqStringWrapper(ses) => Json.fromValues(ses.map(Json.fromString))
      }

    override def apply(c: HCursor): Result[Anomaly.Parameter] = {
      val sa: Result[String] = c.as[String]
      if (sa.isRight) {
        sa.map(Anomaly.Parameter)
      }
      else {
        c.as[List[String]].map(Anomaly.Parameter)
      }
    }
  }

  implicit final private val pureharmAnomalyBaseParamsCodec: Codec[Anomaly.Parameters] =
    new Codec[Anomaly.Parameters] {

      override def apply(c: HCursor): Result[Anomaly.Parameters] = {
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

      override def apply(a: Anomaly.Parameters): Json =
        if (a.isEmpty) {
          Json.fromJsonObject(JsonObject.empty)
        }
        else {
          val parametersJson = a.map { p: (String, Anomaly.Parameter) => (p._1, pureharmStringOrSeqCodec(p._2)) }
          io.circe.Json.fromFields(parametersJson)
        }
    }

  implicit final val pureharmAnomalyBaseCodec: Codec[AnomalyBase] = new Codec[AnomalyBase] {

    override def apply(c: HCursor): Result[AnomalyBase] =
      for {
        id     <- c.get[AnomalyID](PureharmJsonConstants.id)
        msg    <- c.get[String](PureharmJsonConstants.message)
        params <- c.getOrElse[Anomaly.Parameters](PureharmJsonConstants.parameters)(Anomaly.Parameters.empty)
      } yield Anomaly(id, msg, params)

    override def apply(a: AnomalyBase): Json = {
      val id      = AnomalyIDCodec(a.id)
      val message = Json.fromString(a.message)
      if (a.parameters.isEmpty) {
        Json.obj(
          PureharmJsonConstants.id      -> id,
          PureharmJsonConstants.message -> message,
        )
      }
      else {
        val params = pureharmAnomalyBaseParamsCodec(a.parameters)
        Json.obj(
          PureharmJsonConstants.id         -> id,
          PureharmJsonConstants.message    -> message,
          PureharmJsonConstants.parameters -> params,
        )
      }
    }
  }

  implicit final val AnomaliesCodec: Codec[AnomaliesBase] = new Codec[AnomaliesBase] {

    override def apply(a: AnomaliesBase): Json = {
      val fm          = (pureharmAnomalyBaseCodec: Encoder[AnomalyBase])(a)
      val arr         = a.messages.map(pureharmAnomalyBaseCodec.apply)
      val messagesObj = Json.obj(PureharmJsonConstants.messages -> Json.arr(arr: _*))
      messagesObj.deepMerge(fm)
    }

    override def apply(c: HCursor): Result[AnomaliesBase] =
      for {
        fm   <- c.as[AnomalyBase]
        msgs <- c.get[Seq[AnomalyBase]](PureharmJsonConstants.messages)
        _    <-
          if (msgs.isEmpty)
            Left(DecodingFailure("Anomalies.message needs to be non empty array", c.history))
          else
            Right.apply(())
      } yield Anomalies(fm.id, fm.message, Anomaly(msgs.head), msgs.tail.map(a => Anomaly(a)): _*)
  }
}
