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
package busymachines.pureharm.json

import io.circe.generic.extras.codec.ConfiguredAsObjectCodec
import io.circe.generic.extras.{decoding, encoding, semiauto => circeSemiAuto}
import shapeless.Lazy

/**
  *
  * Code mostly gotten from:
  * [[io.circe.generic.extras.semiauto.DerivationHelper]],
  * these are just aliases + some extra to help along the pureharm-style
  * of doing work.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
trait SemiAutoDerivation {

  final type DerivationHelper[A] = io.circe.generic.extras.semiauto.DerivationHelper[A]

  final def decoder[A](implicit decode: Lazy[decoding.ConfiguredDecoder[A]]): Decoder[A] =
    circeSemiAuto.deriveConfiguredDecoder[A](decode)

  final def encoder[A](implicit encode: Lazy[encoding.ConfiguredAsObjectEncoder[A]]): Encoder.AsObject[A] =
    circeSemiAuto.deriveConfiguredEncoder[A](encode)

  final def codec[A](implicit codec: Lazy[ConfiguredAsObjectCodec[A]]): Codec.AsObject[A] = codec.value

  final def deriveFor[A]: DerivationHelper[A] =
    circeSemiAuto.deriveConfiguredFor[A]

  final def enumerationDecoder[A](implicit decode: Lazy[decoding.EnumerationDecoder[A]]): Decoder[A] =
    circeSemiAuto.deriveEnumerationDecoder[A]

  final def enumerationEncoder[A](implicit encode: Lazy[encoding.EnumerationEncoder[A]]): Encoder[A] =
    circeSemiAuto.deriveEnumerationEncoder[A]

  final def enumerationCodec[A](
    implicit
    encode: Lazy[encoding.EnumerationEncoder[A]],
    decode: Lazy[decoding.EnumerationDecoder[A]],
  ): Codec[A] = Codec.from[A](decode.value, encode.value)
}
