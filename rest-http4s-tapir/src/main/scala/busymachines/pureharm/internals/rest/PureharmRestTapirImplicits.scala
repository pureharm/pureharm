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
package busymachines.pureharm.internals.rest

import java.util.UUID

import busymachines.pureharm.phantom.{SafeSpook, Spook}
import sttp.tapir

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */

trait PureharmRestTapirImplicits extends sttp.tapir.json.circe.TapirJsonCirce {

  implicit def phantomTypeTapirStringPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[String],
    p:  Spook[String, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[String, PT]

  implicit def phantomTypeTapirUUIDPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[UUID],
    p:  Spook[UUID, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[UUID, PT]

  implicit def phantomTypeTapirLongPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[Long],
    p:  Spook[Long, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[Long, PT]

  implicit def phantomTypeTapirIntPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[Int],
    p:  Spook[Int, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[Int, PT]

  implicit def phantomTypeTapirBytePathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[Byte],
    p:  Spook[Byte, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[Byte, PT]

  implicit def phantomTypeTapirShortPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[Short],
    p:  Spook[Short, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[Short, PT]

  implicit def phantomTypeTapirBooleanPathCodec[PT](implicit
    tc: tapir.Codec.PlainCodec[Boolean],
    p:  Spook[Boolean, PT],
  ): tapir.Codec.PlainCodec[PT] = genericPhantomTypePathMatcher[Boolean, PT]

  @inline private def genericPhantomTypePathMatcher[Underlying, PT](implicit
    tc: tapir.Codec.PlainCodec[Underlying],
    p:  Spook[Underlying, PT],
  ): tapir.Codec.PlainCodec[PT] = tc.map(p.spook _)(p.despook)

  //---------------------------------------------------------------------------
  //---------------------------------------------------------------------------
  //---------------------------------------------------------------------------

  implicit def phantomTypeGenericSchema[Underlying, PT: Spook[Underlying, *]](implicit
    sc: tapir.Schema[Underlying]
  ): tapir.Schema[PT] =
    sc.copy(description = sc.description match {
      case None           => Option(Spook[Underlying, PT].symbolicName)
      case Some(original) => Option(s"$original — type name: ${Spook[Underlying, PT].symbolicName}")
    })

  implicit def safePhantomTypeGenericSchema[E, Underlying, PT: SafeSpook[E, Underlying, *]](implicit
    sc: tapir.Schema[Underlying]
  ): tapir.Schema[PT] =
    sc.copy(description = sc.description match {
      case None           => Option(SafeSpook[E, Underlying, PT].symbolicName)
      case Some(original) => Option(s"$original — type name: ${SafeSpook[E, Underlying, PT].symbolicName}")
    })

  //---------------------------------------------------------------------------
  //---------------------------------------------------------------------------
  //---------------------------------------------------------------------------

  implicit def phantomTypeGenericValidator[Underlying, PT: Spook[Underlying, *]](implicit
    sc: tapir.Validator[Underlying]
  ): tapir.Validator[PT] = sc.contramap(Spook[Underlying, PT].despook)

  implicit def safePhantomTypeGenericValidator[E, Underlying, PT: SafeSpook[E, Underlying, *]](implicit
    sc: tapir.Validator[Underlying]
  ): tapir.Validator[PT] = sc.contramap(SafeSpook[E, Underlying, PT].despook)

  /** Basically, it's union of the schema of AnomalyBase and AnomaliesBase,
    * + any non-anomaly throwable is being wrapped in an UnhandledAnomaly
    */
  implicit val tapirSchemaThrowableAnomaly: tapir.Schema[Throwable] = PureharmTapirSchemas.tapirSchemaAnomalies

  implicit def pureharmTapirAuthOps(o: tapir.TapirAuth.type): TapirOps.AuthOps = new TapirOps.AuthOps(o)
}
