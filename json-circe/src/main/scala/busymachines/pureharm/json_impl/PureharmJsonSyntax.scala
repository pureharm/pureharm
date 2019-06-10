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
package busymachines.pureharm.json_impl

import io.circe._

import busymachines.pureharm.effects._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
object PureharmJsonSyntax {

  trait Implicits {
    implicit final def bmcJsonEncoderOps[A](wrappedEncodeable: A): EncoderOps[A] =
      new EncoderOps(wrappedEncodeable)

    implicit final def bmcJsonDecoderOpsString(rawJson: String): DecoderOpsString =
      new DecoderOpsString(rawJson)

    implicit final def bmcJsonDecoderOpsJson(js: Json): DecoderOpsJson =
      new DecoderOpsJson(js)
  }

  final class EncoderOps[A](val wrappedEncodeable: A) extends AnyVal {
    def asJson(implicit encoder: Encoder[A]): Json = encoder(wrappedEncodeable)

    def asJsonObject(implicit encoder: ObjectEncoder[A]): JsonObject =
      encoder.encodeObject(wrappedEncodeable)
  }

  final class DecoderOpsString(val rawJson: String) extends AnyVal {
    def unsafeAsJson: Json = JsonParsing.unsafeParseString(rawJson)

    def unsafeDecodeAs[A](implicit decoder: Decoder[A]): A =
      JsonDecoding.unsafeDecodeAs[A](rawJson)

    def decodeAs[A](implicit decoder: Decoder[A]): Attempt[A] = {
      JsonDecoding.decodeAs[A](rawJson)
    }
  }

  final class DecoderOpsJson(val js: Json) extends AnyVal {

    def unsafeDecodeAs[A](implicit decoder: Decoder[A]): A =
      JsonDecoding.unsafeDecodeAs[A](js)

    def decodeAs[A](implicit decoder: Decoder[A]): Attempt[A] = {
      JsonDecoding.decodeAs[A](js)
    }

    def noSpacesNoNulls: String = js.pretty(PrettyJson.noSpacesNoNulls)

    def spaces2NoNulls: String = js.pretty(PrettyJson.spaces2NoNulls)

    def spaces4NoNulls: String = js.pretty(PrettyJson.spaces4NoNulls)
  }

}
