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

import io.circe.{Decoder, Encoder}
import shapeless.tag.@@

/**
  *
  * Unfortunately type inference rarely (if ever) works
  * with something fully generic like
  * {{{
  *     trait LowPriorityPhantomTypeInstances {
  *
  *     final implicit def genericPhantomTypeEncoder[Tag, T](implicit enc: Encoder[T]): Encoder[T @@ Tag] =
  *       enc.asInstanceOf[Encoder[T @@ Tag]]
  *
  *     final implicit def genericPhantomTypeDecoder[Tag, T](implicit dec: Decoder[T]): Decoder[T @@ Tag] =
  *       dec.asInstanceOf[Decoder[T @@ Tag]]
  *   }
  * }}}
  *
  * And in the case of [[PureharmJsonInstances.PhantomTypeCollectionInstances]] it even
  * conflicted with the explicit definitions!!
  *
  * At some point in time another crack will have to be taken to implement
  * the fully generic version, but for now giving up...
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
object PureharmJsonInstances {

  trait Implicits extends PhantomTypeInstances

  trait PhantomTypeInstances
      extends PhantomTypePrimitiveInstances with PhantomTypeCollectionInstances with PhantomTypeScalaDurationInstances
      with PhantomTypeJavaTimeInstances with PhantomTypeTupleInstances with PhantomTypeJavaMiscInstances

  trait PhantomTypePrimitiveInstances {

    implicit final def stringPhantomTypeEncoder[Tag]: Encoder[String @@ Tag] =
      Encoder[String].asInstanceOf[Encoder[String @@ Tag]]

    implicit final def stringPhantomTypeDecoder[Tag]: Decoder[String @@ Tag] =
      Decoder[String].asInstanceOf[Decoder[String @@ Tag]]

    implicit final def booleanPhantomTypeEncoder[Tag]: Encoder[Boolean @@ Tag] =
      Encoder[Boolean].asInstanceOf[Encoder[Boolean @@ Tag]]

    implicit final def booleanPhantomTypeDecoder[Tag]: Decoder[Boolean @@ Tag] =
      Decoder[Boolean].asInstanceOf[Decoder[Boolean @@ Tag]]

    implicit final def bytePhantomTypeEncoder[Tag]: Encoder[Byte @@ Tag] =
      Encoder[Byte].asInstanceOf[Encoder[Byte @@ Tag]]

    implicit final def bytePhantomTypeDecoder[Tag]: Decoder[Byte @@ Tag] =
      Decoder[Byte].asInstanceOf[Decoder[Byte @@ Tag]]

    implicit final def shortPhantomTypeEncoder[Tag]: Encoder[Short @@ Tag] =
      Encoder[Short].asInstanceOf[Encoder[Short @@ Tag]]

    implicit final def shortPhantomTypeDecoder[Tag]: Decoder[Short @@ Tag] =
      Decoder[Short].asInstanceOf[Decoder[Short @@ Tag]]

    implicit final def charPhantomTypeEncoder[Tag]: Encoder[Char @@ Tag] =
      Encoder[Char].asInstanceOf[Encoder[Char @@ Tag]]

    implicit final def charPhantomTypeDecoder[Tag]: Decoder[Char @@ Tag] =
      Decoder[Char].asInstanceOf[Decoder[Char @@ Tag]]

    implicit final def intPhantomTypeEncoder[Tag]: Encoder[Int @@ Tag] =
      Encoder[Int].asInstanceOf[Encoder[Int @@ Tag]]

    implicit final def intPhantomTypeDecoder[Tag]: Decoder[Int @@ Tag] =
      Decoder[Int].asInstanceOf[Decoder[Int @@ Tag]]

    implicit final def longPhantomTypeEncoder[Tag]: Encoder[Long @@ Tag] =
      Encoder[Long].asInstanceOf[Encoder[Long @@ Tag]]

    implicit final def longPhantomTypeDecoder[Tag]: Decoder[Long @@ Tag] =
      Decoder[Long].asInstanceOf[Decoder[Long @@ Tag]]

    implicit final def floatPhantomTypeEncoder[Tag]: Encoder[Float @@ Tag] =
      Encoder[Float].asInstanceOf[Encoder[Float @@ Tag]]

    implicit final def floatPhantomTypeDecoder[Tag]: Decoder[Float @@ Tag] =
      Decoder[Float].asInstanceOf[Decoder[Float @@ Tag]]

    implicit final def doublePhantomTypeEncoder[Tag]: Encoder[Double @@ Tag] =
      Encoder[Double].asInstanceOf[Encoder[Double @@ Tag]]

    implicit final def doublePhantomTypeDecoder[Tag]: Decoder[Double @@ Tag] =
      Decoder[Double].asInstanceOf[Decoder[Double @@ Tag]]

    implicit final def bigDecimalPhantomTypeEncoder[Tag]: Encoder[BigDecimal @@ Tag] =
      Encoder[BigDecimal].asInstanceOf[Encoder[BigDecimal @@ Tag]]

    implicit final def bigDecimalPhantomTypeDecoder[Tag]: Decoder[BigDecimal @@ Tag] =
      Decoder[BigDecimal].asInstanceOf[Decoder[BigDecimal @@ Tag]]

    implicit final def bigIntPhantomTypeEncoder[Tag]: Encoder[BigInt @@ Tag] =
      Encoder[BigInt].asInstanceOf[Encoder[BigInt @@ Tag]]

    implicit final def bigIntPhantomTypeDecoder[Tag]: Decoder[BigInt @@ Tag] =
      Decoder[BigInt].asInstanceOf[Decoder[BigInt @@ Tag]]
  }

  trait PhantomTypeCollectionInstances {
    import cats.data.{NonEmptyList, NonEmptyMap, NonEmptySet}

    implicit final def phantomTypeListEncoder[Tag, T](implicit ls: Encoder[List[T]]): Encoder[List[T] @@ Tag] =
      ls.asInstanceOf[Encoder[List[T] @@ Tag]]

    implicit final def phantomTypeListDecoder[Tag, T](implicit ls: Decoder[List[T]]): Decoder[List[T] @@ Tag] =
      ls.asInstanceOf[Decoder[List[T] @@ Tag]]

    implicit final def phantomTypeNEListEncoder[Tag, T](
      implicit ls: Encoder[NonEmptyList[T]],
    ): Encoder[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptyList[T] @@ Tag]]

    implicit final def phantomTypeNEListDecoder[Tag, T](
      implicit ls: Decoder[NonEmptyList[T]],
    ): Decoder[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptyList[T] @@ Tag]]

    implicit final def phantomTypeSetEncoder[Tag, T](implicit ls: Encoder[Set[T]]): Encoder[Set[T] @@ Tag] =
      ls.asInstanceOf[Encoder[Set[T] @@ Tag]]

    implicit final def phantomTypeSetDecoder[Tag, T](implicit ls: Decoder[Set[T]]): Decoder[Set[T] @@ Tag] =
      ls.asInstanceOf[Decoder[Set[T] @@ Tag]]

    implicit final def phantomTypeNESetEncoder[Tag, T](
      implicit ls: Encoder[NonEmptySet[T]],
    ): Encoder[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptySet[T] @@ Tag]]

    implicit final def phantomTypeNESetDecoder[Tag, T](
      implicit ls: Decoder[NonEmptySet[T]],
    ): Decoder[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptySet[T] @@ Tag]]

    implicit final def phantomTypeMapEncoder[Tag, K, V](implicit ls: Encoder[Map[K, V]]): Encoder[Map[K, V] @@ Tag] =
      ls.asInstanceOf[Encoder[Map[K, V] @@ Tag]]

    implicit final def phantomTypeMapDecoder[Tag, K, V](implicit ls: Decoder[Map[K, V]]): Decoder[Map[K, V] @@ Tag] =
      ls.asInstanceOf[Decoder[Map[K, V] @@ Tag]]

    implicit final def phantomTypeNEMapEncoder[Tag, K, V](
      implicit ls: Encoder[NonEmptyMap[K, V]],
    ): Encoder[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptyMap[K, V] @@ Tag]]

    implicit final def phantomTypeNEMapDecoder[Tag, K, V](
      implicit ls: Decoder[NonEmptyMap[K, V]],
    ): Decoder[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptyMap[K, V] @@ Tag]]
  }

  trait PhantomTypeScalaDurationInstances {
    import scala.concurrent.duration._

    implicit final def sdDurationPhantomTypeEncoder[Tag](implicit enc: Encoder[Duration]): Encoder[Duration @@ Tag] =
      enc.asInstanceOf[Encoder[Duration @@ Tag]]

    implicit final def sdDurationPhantomTypeDecoder[Tag](implicit dec: Decoder[Duration]): Decoder[Duration @@ Tag] =
      dec.asInstanceOf[Decoder[Duration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeEncoder[Tag](
      implicit enc: Encoder[FiniteDuration],
    ): Encoder[FiniteDuration @@ Tag] =
      enc.asInstanceOf[Encoder[FiniteDuration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeDecoder[Tag](
      implicit dec: Decoder[FiniteDuration],
    ): Decoder[FiniteDuration @@ Tag] =
      dec.asInstanceOf[Decoder[FiniteDuration @@ Tag]]

    implicit final def sdDeadlinePhantomTypeEncoder[Tag](implicit enc: Encoder[Deadline]): Encoder[Deadline @@ Tag] =
      enc.asInstanceOf[Encoder[Deadline @@ Tag]]

    implicit final def sdDeadlinePhantomTypeDecoder[Tag](implicit dec: Decoder[Deadline]): Decoder[Deadline @@ Tag] =
      dec.asInstanceOf[Decoder[Deadline @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {
    import java.time._

    implicit final def jtDurationPhantomTypeEncoder[Tag](implicit enc: Encoder[Duration]): Encoder[Duration @@ Tag] =
      enc.asInstanceOf[Encoder[Duration @@ Tag]]

    implicit final def jtDurationPhantomTypeDecoder[Tag](implicit dec: Decoder[Duration]): Decoder[Duration @@ Tag] =
      dec.asInstanceOf[Decoder[Duration @@ Tag]]

    implicit final def jtInstantPhantomTypeEncoder[Tag](implicit enc: Encoder[Instant]): Encoder[Instant @@ Tag] =
      enc.asInstanceOf[Encoder[Instant @@ Tag]]

    implicit final def jtInstantPhantomTypeDecoder[Tag](implicit dec: Decoder[Instant]): Decoder[Instant @@ Tag] =
      dec.asInstanceOf[Decoder[Instant @@ Tag]]

    implicit final def jtLocalDatePhantomTypeEncoder[Tag](implicit enc: Encoder[LocalDate]): Encoder[LocalDate @@ Tag] =
      enc.asInstanceOf[Encoder[LocalDate @@ Tag]]

    implicit final def jtLocalDatePhantomTypeDecoder[Tag](implicit dec: Decoder[LocalDate]): Decoder[LocalDate @@ Tag] =
      dec.asInstanceOf[Decoder[LocalDate @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[LocalDateTime],
    ): Encoder[LocalDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[LocalDateTime @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeDecoder[Tag](
      implicit dec: Decoder[LocalDateTime],
    ): Decoder[LocalDateTime @@ Tag] =
      dec.asInstanceOf[Decoder[LocalDateTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeEncoder[Tag](implicit enc: Encoder[LocalTime]): Encoder[LocalTime @@ Tag] =
      enc.asInstanceOf[Encoder[LocalTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeDecoder[Tag](implicit dec: Decoder[LocalTime]): Decoder[LocalTime @@ Tag] =
      dec.asInstanceOf[Decoder[LocalTime @@ Tag]]

    implicit final def jtMonthDayPhantomTypeEncoder[Tag](implicit enc: Encoder[MonthDay]): Encoder[MonthDay @@ Tag] =
      enc.asInstanceOf[Encoder[MonthDay @@ Tag]]

    implicit final def jtMonthDayPhantomTypeDecoder[Tag](implicit dec: Decoder[MonthDay]): Decoder[MonthDay @@ Tag] =
      dec.asInstanceOf[Decoder[MonthDay @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[OffsetDateTime],
    ): Encoder[OffsetDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeDecoder[Tag](
      implicit dec: Decoder[OffsetDateTime],
    ): Decoder[OffsetDateTime @@ Tag] =
      dec.asInstanceOf[Decoder[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[Duration],
    ): Encoder[OffsetTime @@ Tag] =
      enc.asInstanceOf[Encoder[OffsetTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeDecoder[Tag](
      implicit dec: Decoder[Duration],
    ): Decoder[OffsetTime @@ Tag] =
      dec.asInstanceOf[Decoder[OffsetTime @@ Tag]]

    implicit final def jtPeriodPhantomTypeEncoder[Tag](implicit enc: Encoder[Period]): Encoder[Period @@ Tag] =
      enc.asInstanceOf[Encoder[Period @@ Tag]]

    implicit final def jtPeriodPhantomTypeDecoder[Tag](implicit dec: Decoder[Period]): Decoder[Period @@ Tag] =
      dec.asInstanceOf[Decoder[Period @@ Tag]]

    implicit final def jtYearPhantomTypeEncoder[Tag](implicit enc: Encoder[Year]): Encoder[Year @@ Tag] =
      enc.asInstanceOf[Encoder[Year @@ Tag]]

    implicit final def jtYearMonthPhantomTypeDecoder[Tag](implicit dec: Decoder[YearMonth]): Decoder[YearMonth @@ Tag] =
      dec.asInstanceOf[Decoder[YearMonth @@ Tag]]

    implicit final def jtZonedDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[ZonedDateTime],
    ): Encoder[ZonedDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[ZonedDateTime @@ Tag]]

    implicit final def jtZoneIdPhantomTypeDecoder[Tag](implicit dec: Decoder[ZoneId]): Decoder[ZoneId @@ Tag] =
      dec.asInstanceOf[Decoder[ZoneId @@ Tag]]

    implicit final def jtZoneOffsetPhantomTypeEncoder[Tag](
      implicit enc: Encoder[ZoneOffset],
    ): Encoder[ZoneOffset @@ Tag] =
      enc.asInstanceOf[Encoder[ZoneOffset @@ Tag]]

  }

  trait PhantomTypeTupleInstances {
    implicit final def tuple1PhantomTypeEncoder[T1, Tag](
      implicit enc: Encoder[Tuple1[T1]],
    ): Encoder[Tuple1[T1] @@ Tag] =
      enc.asInstanceOf[Encoder[Tuple1[T1] @@ Tag]]

    implicit final def tuple1PhantomTypeDecoder[T1, Tag](
      implicit dec: Decoder[Tuple1[T1]],
    ): Decoder[Tuple1[T1] @@ Tag] =
      dec.asInstanceOf[Decoder[Tuple1[T1] @@ Tag]]

    implicit final def tuple2PhantomTypeEncoder[T1, T2, Tag](
      implicit enc: Encoder[(T1, T2)],
    ): Encoder[(T1, T2) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2) @@ Tag]]

    implicit final def tuple2PhantomTypeDecoder[T1, T2, Tag](
      implicit dec: Decoder[(T1, T2)],
    ): Decoder[(T1, T2) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2) @@ Tag]]

    implicit final def tuple3PhantomTypeEncoder[T1, T2, T3, Tag](
      implicit enc: Encoder[(T1, T2, T3)],
    ): Encoder[(T1, T2, T3) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2, T3) @@ Tag]]

    implicit final def tuple3PhantomTypeDecoder[T1, T2, T3, Tag](
      implicit dec: Decoder[(T1, T2, T3)],
    ): Decoder[(T1, T2, T3) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2, T3) @@ Tag]]

    implicit final def tuple4PhantomTypeEncoder[T1, T2, T3, T4, Tag](
      implicit enc: Encoder[(T1, T2, T3, T4)],
    ): Encoder[(T1, T2, T3, T4) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2, T3, T4) @@ Tag]]

    implicit final def tuple4PhantomTypeDecoder[T1, T2, T3, T4, Tag](
      implicit dec: Decoder[(T1, T2, T3, T4)],
    ): Decoder[(T1, T2, T3, T4) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2, T3, T4) @@ Tag]]

    //TODO: all tuples
  }

  trait PhantomTypeJavaMiscInstances {
    import java.util.UUID
    implicit final def miscUUIDPhantomTypeEncoder[Tag](implicit enc: Encoder[UUID]): Encoder[UUID @@ Tag] =
      enc.asInstanceOf[Encoder[UUID @@ Tag]]

    implicit final def miscUUIDPhantomTypeDecoder[Tag](implicit dec: Decoder[UUID]): Decoder[UUID @@ Tag] =
      dec.asInstanceOf[Decoder[UUID @@ Tag]]

  }

}
