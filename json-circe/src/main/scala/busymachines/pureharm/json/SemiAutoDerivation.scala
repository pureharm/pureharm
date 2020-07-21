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

import scala.annotation.implicitNotFound

/**
  * Code mostly gotten from:
  * [[io.circe.generic.extras.semiauto.DerivationHelper]],
  * these are just aliases + some extra to help along the pureharm-style
  * of doing work.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
trait SemiAutoDerivation {
  final type DerivationHelper[A] = io.circe.generic.extras.semiauto.DerivationHelper[A]

  final def decoder[A](implicit
    // format: off
  @implicitNotFound(
"""
Decoder[${A}] failed to be derived.
_
Deriving decoders in pureharm style can run into one of the several problems:
   1) if your case class has any PhantomType[T] for any T, then make sure you have an Decoder (xor Codec) for T, AND the PureharmJsonImplicits in scope, usually by mixing that trait into your myapp.json.implicits._ and importing it.
   _
   2) if your case class has some field of type SafePhantomType[E, T], you need to have a Show[E] for its error, since circe uses Strings for errors. Usually, if your error is a Throwable, pureharm.effects.implicits._ contains Show[Throwable] otherwise roll your own.
   _
   3) Make sure you have one, and only one io.circe.generic.extras.Configuration in scope. In pureharm, this is usually brought int in by mixing in PureharmJsonImplicits into your myapp.json.implicits package.
   _
   4) As usual for circe, make sure you have in scope Decoder (xor Codec) for any type that is not a case class. To debug write step by step explicit implicits for your case class field types w/ stubs until you narrow down your problem, eg:
      case class F(f1: F1, f2: F2)
      _
      import myapp.json.implcits._
      val decoderF = derive.decoder[F] //fails
      _
      to debug:
      implicit val tempf1Decoder: Decoder[F1] = ???
      val decoderF = derive.decoder[F] //if it works now, you know F1 didn't have a decoder, your turn to find it.
      _
      Usually, using stuff from the following things trip people up since they don't have defaults:
        a) types java.time._
        b) java.util.UUID
    5) when deriving a sealed trait, make sure all subtypes are a product, or provide Decoder (xor Codec) for such subtypes
""")
    // format: on
    decode: Lazy[decoding.ConfiguredDecoder[A]]
  ): Decoder[A] =
    circeSemiAuto.deriveConfiguredDecoder[A](decode)

  final def encoder[A](implicit
    // format: off
    @implicitNotFound(
"""
Encoder[${A}] failed to be derived.
_
Deriving encoders in pureharm style can run into one of the several problems:
   1) if your case class has any PhantomType[T]/SafePhantomType[_, T] for any T, then make sure you have an Encoder (xor Codec) for T, AND the PureharmJsonImplicits in scope, usually by mixing that trait into your myapp.json.implicits._ and importing it.
   _
   2) Make sure you have one, and only one io.circe.generic.extras.Configuration in scope. In pureharm, this is usually brought int in by mixing in PureharmJsonImplicits into your myapp.json.implicits package.
   _
   3) As usual for circe, make sure you have in scope Encoder (xor Codec) for any type that is not a case class. To debug write step by step explicit implicits for your case class field types w/ stubs until you narrow down your problem, eg:
      case class F(f1: F1, f2: F2)
      _
      import myapp.json.implcits._
      implicit val encoderF = derive.encoder[F] //fails
      _
      to debug:
      implicit val tempf1Encoder: Encoder[F1] = ???
      //if it works now, you know F1 didn't have a encoder, your turn to find it.
      implicit val encoderF = derive.encoder[F]
      _
      Usually, using stuff from the following things trip people up since they don't have defaults:
        a) java.time._
        b) java.util.UUID
      _
   5) when deriving a sealed trait, make sure all subtypes are a product, or provide Encoder (xor Codec) for such subtypes
""")
    // format: on
    encode: Lazy[encoding.ConfiguredAsObjectEncoder[A]]
  ): Encoder.AsObject[A] =
    circeSemiAuto.deriveConfiguredEncoder[A](encode)

  final def codec[A](implicit
    // format: off
@implicitNotFound(
"""
Codec[${A}] failed to be derived.
_
Deriving codecs in pureharm style can run into one of the several problems:
   1) if your case class has any PhantomType[T] for any T, then make sure you have an Encoder/Decoder (xor Codec) for T, AND the PureharmJsonImplicits in scope, usually by mixing that trait into your myapp.json.implicits._ and importing it.
   _
   2) if your case class has some field of type SafePhantomType[E, T], you need to have a Show[E] for its error, since circe uses Strings for errors. Usually, if your error is a Throwable, pureharm.effects.implicits._ contains Show[Throwable] otherwise roll your own.
   _
   3) Make sure you have one, and only one io.circe.generic.extras.Configuration in scope. In pureharm, this is usually brought int in by mixing in PureharmJsonImplicits into your myapp.json.implicits package.
   _
   4) As usual for circe, make sure you have in scope Encoder/Decoder (xor Codec) for any type that is not a case class. To debug write step by step explicit implicits for your case class field types w/ stubs until you narrow down your problem, eg:
      case class F(f1: F1, f2: F2)
      _
      import myapp.json.implcits._
      val codecF = derive.codec[F] //fails
      _
      to debug:
      implicit val tempf1Encoder: Encoder[F1] = ???
      implicit val tempf2Decoder: Decoder[F2] = ???
      //if it works now, you know F1 didn't have an Encoder, and F2 didn't have a Decoder your turn to find it.
      val codecF = derive.codec[F]
      _
      Usually, using stuff from the following things trip people up since they don't have defaults:
        a) java.time._
        b) java.util.UUID
      //
   5) when deriving a sealed trait, make sure all subtypes are a product, or, provide Codec for such subtypes
""")
    // format: on
    codec: Lazy[ConfiguredAsObjectCodec[A]]
  ): Codec.AsObject[A] = codec.value

  final def enumerationDecoder[A](implicit
    // format: off
    @implicitNotFound(
"""
Decoder[${A}] failed to be derived.
_
Deriving Decoder for enumerations can run into the following problem:
   1) make sure that your sealed trait hierarchy contains subtypes that are only case objects or case classes with zero parameters
""")
    // format: on
    decode: Lazy[decoding.EnumerationDecoder[A]]
  ): Decoder[A] = circeSemiAuto.deriveEnumerationDecoder[A]

  final def enumerationEncoder[A](implicit
    // format: off
  @implicitNotFound(
"""
Encoder[${A}] for sealed trait enumeration failed to be derived.
_
Deriving Encoder for enumerations can run into the following problem:
   1) make sure that your sealed trait hierarchy contains subtypes that are only case objects or case classes with zero parameters
""")
    // format: on
    encode: Lazy[encoding.EnumerationEncoder[A]]
  ): Encoder[A] = circeSemiAuto.deriveEnumerationEncoder[A]

  final def enumerationCodec[A](implicit
    // format: off
  @implicitNotFound(
"""
Codec[${A}] for sealed trait enumeration failed to be derived because could not derive an:
  * Encoder[${A}]
_
Deriving Encoder for enumerations can run into the following problem:
   1) make sure that your sealed trait hierarchy contains subtypes that are only case objects or case classes with zero parameters
""")
    // format: on
    encode: Lazy[encoding.EnumerationEncoder[A]],
    // format: off
    @implicitNotFound(
"""
Codec[${A}] for sealed trait enumeration failed to be derived because could not derive a:
  * Decoder[${A}]
_
Deriving Decoder for enumerations can run into the following problem:
   1) make sure that your sealed trait hierarchy contains subtypes that are only case objects or case classes with zero parameters
""")
    // format: on
    decode: Lazy[decoding.EnumerationDecoder[A]],
  ): Codec[A] = Codec.from[A](decode.value, encode.value)

  final def deriveFor[A]: DerivationHelper[A] =
    circeSemiAuto.deriveConfiguredFor[A]
}
