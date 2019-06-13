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

import io.circe.{Decoder, Encoder, HCursor, Json}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  *
  */
trait Codec[A] extends Encoder[A] with Decoder[A]

object Codec {

  def apply[A](implicit instance: Codec[A]): Codec[A] = instance

  def instance[A](encode: Encoder[A], decode: Decoder[A]): Codec[A] = {
    new Codec[A] {
      private val enc = encode
      private val dec = decode

      override def apply(a: A): Json = enc(a)

      override def apply(c: HCursor): io.circe.Decoder.Result[A] = dec(c)
    }
  }
}
