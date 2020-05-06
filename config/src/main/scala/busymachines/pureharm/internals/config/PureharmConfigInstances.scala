/**
  * Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.config

import pureconfig.{ConfigReader, ConfigWriter}
import shapeless.tag.@@

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
object PureharmConfigInstances {

  trait Implicits extends PhantomTypeInstances

  trait PhantomTypeInstances
      extends PhantomTypePrimitiveInstances with PhantomTypeCollectionInstances with PhantomTypeScalaDurationInstances
      with PhantomTypeJavaTimeInstances with PhantomTypeTupleInstances with PhantomTypeJavaMiscInstances

  trait PhantomTypePrimitiveInstances {

    implicit final def stringPhantomTypeConfigWriter[Tag]: ConfigWriter[String @@ Tag] =
      ConfigWriter[String].asInstanceOf[ConfigWriter[String @@ Tag]]

    implicit final def stringPhantomTypeConfigReader[Tag]: ConfigReader[String @@ Tag] =
      ConfigReader[String].asInstanceOf[ConfigReader[String @@ Tag]]

    implicit final def booleanPhantomTypeConfigWriter[Tag]: ConfigWriter[Boolean @@ Tag] =
      ConfigWriter[Boolean].asInstanceOf[ConfigWriter[Boolean @@ Tag]]

    implicit final def booleanPhantomTypeConfigReader[Tag]: ConfigReader[Boolean @@ Tag] =
      ConfigReader[Boolean].asInstanceOf[ConfigReader[Boolean @@ Tag]]

    implicit final def bytePhantomTypeConfigWriter[Tag](implicit c: ConfigWriter[Byte]): ConfigWriter[Byte @@ Tag] =
      c.asInstanceOf[ConfigWriter[Byte @@ Tag]]

    implicit final def bytePhantomTypeConfigReader[Tag](implicit c: ConfigReader[Byte]): ConfigReader[Byte @@ Tag] =
      c.asInstanceOf[ConfigReader[Byte @@ Tag]]

    implicit final def shortPhantomTypeConfigWriter[Tag]: ConfigWriter[Short @@ Tag] =
      ConfigWriter[Short].asInstanceOf[ConfigWriter[Short @@ Tag]]

    implicit final def shortPhantomTypeConfigReader[Tag]: ConfigReader[Short @@ Tag] =
      ConfigReader[Short].asInstanceOf[ConfigReader[Short @@ Tag]]

    implicit final def charPhantomTypeConfigWriter[Tag]: ConfigWriter[Char @@ Tag] =
      ConfigWriter[Char].asInstanceOf[ConfigWriter[Char @@ Tag]]

    implicit final def charPhantomTypeConfigReader[Tag]: ConfigReader[Char @@ Tag] =
      ConfigReader[Char].asInstanceOf[ConfigReader[Char @@ Tag]]

    implicit final def intPhantomTypeConfigWriter[Tag]: ConfigWriter[Int @@ Tag] =
      ConfigWriter[Int].asInstanceOf[ConfigWriter[Int @@ Tag]]

    implicit final def intPhantomTypeConfigReader[Tag]: ConfigReader[Int @@ Tag] =
      ConfigReader[Int].asInstanceOf[ConfigReader[Int @@ Tag]]

    implicit final def longPhantomTypeConfigWriter[Tag]: ConfigWriter[Long @@ Tag] =
      ConfigWriter[Long].asInstanceOf[ConfigWriter[Long @@ Tag]]

    implicit final def longPhantomTypeConfigReader[Tag]: ConfigReader[Long @@ Tag] =
      ConfigReader[Long].asInstanceOf[ConfigReader[Long @@ Tag]]

    implicit final def floatPhantomTypeConfigWriter[Tag]: ConfigWriter[Float @@ Tag] =
      ConfigWriter[Float].asInstanceOf[ConfigWriter[Float @@ Tag]]

    implicit final def floatPhantomTypeConfigReader[Tag]: ConfigReader[Float @@ Tag] =
      ConfigReader[Double].asInstanceOf[ConfigReader[Float @@ Tag]]

    implicit final def doublePhantomTypeConfigWriter[Tag]: ConfigWriter[Double @@ Tag] =
      ConfigWriter[Double].asInstanceOf[ConfigWriter[Double @@ Tag]]

    implicit final def doublePhantomTypeConfigReader[Tag]: ConfigReader[Double @@ Tag] =
      ConfigReader[Double].asInstanceOf[ConfigReader[Double @@ Tag]]

    implicit final def bigDecimalPhantomTypeConfigWriter[Tag]: ConfigWriter[BigDecimal @@ Tag] =
      ConfigWriter[BigDecimal].asInstanceOf[ConfigWriter[BigDecimal @@ Tag]]

    implicit final def bigDecimalPhantomTypeConfigReader[Tag]: ConfigReader[BigDecimal @@ Tag] =
      ConfigReader[BigDecimal].asInstanceOf[ConfigReader[BigDecimal @@ Tag]]

    implicit final def bigIntPhantomTypeConfigWriter[Tag]: ConfigWriter[BigInt @@ Tag] =
      ConfigWriter[BigInt].asInstanceOf[ConfigWriter[BigInt @@ Tag]]

    implicit final def bigIntPhantomTypeConfigReader[Tag]: ConfigReader[BigInt @@ Tag] =
      ConfigReader[BigInt].asInstanceOf[ConfigReader[BigInt @@ Tag]]
  }

  trait PhantomTypeCollectionInstances {
    import cats.data.{NonEmptyList, NonEmptyMap, NonEmptySet}

    implicit final def phantomTypeListConfigWriter[Tag, T](
      implicit ls: ConfigWriter[List[T]],
    ): ConfigWriter[List[T] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[List[T] @@ Tag]]

    implicit final def phantomTypeListConfigReader[Tag, T](
      implicit ls: ConfigReader[List[T]],
    ): ConfigReader[List[T] @@ Tag] =
      ls.asInstanceOf[ConfigReader[List[T] @@ Tag]]

    implicit final def phantomTypeNEListConfigWriter[Tag, T](
      implicit ls: ConfigWriter[NonEmptyList[T]],
    ): ConfigWriter[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[NonEmptyList[T] @@ Tag]]

    implicit final def phantomTypeNEListConfigReader[Tag, T](
      implicit ls: ConfigReader[NonEmptyList[T]],
    ): ConfigReader[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[ConfigReader[NonEmptyList[T] @@ Tag]]

    implicit final def phantomTypeSetConfigWriter[Tag, T](
      implicit ls: ConfigWriter[Set[T]],
    ): ConfigWriter[Set[T] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[Set[T] @@ Tag]]

    implicit final def phantomTypeSetConfigReader[Tag, T](
      implicit ls: ConfigReader[Set[T]],
    ): ConfigReader[Set[T] @@ Tag] =
      ls.asInstanceOf[ConfigReader[Set[T] @@ Tag]]

    implicit final def phantomTypeNESetConfigWriter[Tag, T](
      implicit ls: ConfigWriter[NonEmptySet[T]],
    ): ConfigWriter[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[NonEmptySet[T] @@ Tag]]

    implicit final def phantomTypeNESetConfigReader[Tag, T](
      implicit ls: ConfigReader[NonEmptySet[T]],
    ): ConfigReader[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[ConfigReader[NonEmptySet[T] @@ Tag]]

    implicit final def phantomTypeMapConfigWriter[Tag, K, V](
      implicit ls: ConfigWriter[Map[K, V]],
    ): ConfigWriter[Map[K, V] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[Map[K, V] @@ Tag]]

    implicit final def phantomTypeMapConfigReader[Tag, K, V](
      implicit ls: ConfigReader[Map[K, V]],
    ): ConfigReader[Map[K, V] @@ Tag] =
      ls.asInstanceOf[ConfigReader[Map[K, V] @@ Tag]]

    implicit final def phantomTypeNEMapConfigWriter[Tag, K, V](
      implicit ls: ConfigWriter[NonEmptyMap[K, V]],
    ): ConfigWriter[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[ConfigWriter[NonEmptyMap[K, V] @@ Tag]]

    implicit final def phantomTypeNEMapConfigReader[Tag, K, V](
      implicit ls: ConfigReader[NonEmptyMap[K, V]],
    ): ConfigReader[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[ConfigReader[NonEmptyMap[K, V] @@ Tag]]
  }

  trait PhantomTypeScalaDurationInstances {
    import scala.concurrent.duration._

    implicit final def sdDurationPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Duration],
    ): ConfigWriter[Duration @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Duration @@ Tag]]

    implicit final def sdDurationPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Duration],
    ): ConfigReader[Duration @@ Tag] =
      dec.asInstanceOf[ConfigReader[Duration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[FiniteDuration],
    ): ConfigWriter[FiniteDuration @@ Tag] =
      enc.asInstanceOf[ConfigWriter[FiniteDuration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[FiniteDuration],
    ): ConfigReader[FiniteDuration @@ Tag] =
      dec.asInstanceOf[ConfigReader[FiniteDuration @@ Tag]]

    implicit final def sdDeadlinePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Deadline],
    ): ConfigWriter[Deadline @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Deadline @@ Tag]]

    implicit final def sdDeadlinePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Deadline],
    ): ConfigReader[Deadline @@ Tag] =
      dec.asInstanceOf[ConfigReader[Deadline @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {
    import java.time._

    implicit final def jtDurationPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Duration],
    ): ConfigWriter[Duration @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Duration @@ Tag]]

    implicit final def jtDurationPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Duration],
    ): ConfigReader[Duration @@ Tag] =
      dec.asInstanceOf[ConfigReader[Duration @@ Tag]]

    implicit final def jtInstantPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Instant],
    ): ConfigWriter[Instant @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Instant @@ Tag]]

    implicit final def jtInstantPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Instant],
    ): ConfigReader[Instant @@ Tag] =
      dec.asInstanceOf[ConfigReader[Instant @@ Tag]]

    implicit final def jtLocalDatePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[LocalDate],
    ): ConfigWriter[LocalDate @@ Tag] =
      enc.asInstanceOf[ConfigWriter[LocalDate @@ Tag]]

    implicit final def jtLocalDatePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[LocalDate],
    ): ConfigReader[LocalDate @@ Tag] =
      dec.asInstanceOf[ConfigReader[LocalDate @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[LocalDateTime],
    ): ConfigWriter[LocalDateTime @@ Tag] =
      enc.asInstanceOf[ConfigWriter[LocalDateTime @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[LocalDateTime],
    ): ConfigReader[LocalDateTime @@ Tag] =
      dec.asInstanceOf[ConfigReader[LocalDateTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[LocalTime],
    ): ConfigWriter[LocalTime @@ Tag] =
      enc.asInstanceOf[ConfigWriter[LocalTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[LocalTime],
    ): ConfigReader[LocalTime @@ Tag] =
      dec.asInstanceOf[ConfigReader[LocalTime @@ Tag]]

    implicit final def jtMonthDayPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[MonthDay],
    ): ConfigWriter[MonthDay @@ Tag] =
      enc.asInstanceOf[ConfigWriter[MonthDay @@ Tag]]

    implicit final def jtMonthDayPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[MonthDay],
    ): ConfigReader[MonthDay @@ Tag] =
      dec.asInstanceOf[ConfigReader[MonthDay @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[OffsetDateTime],
    ): ConfigWriter[OffsetDateTime @@ Tag] =
      enc.asInstanceOf[ConfigWriter[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[OffsetDateTime],
    ): ConfigReader[OffsetDateTime @@ Tag] =
      dec.asInstanceOf[ConfigReader[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Duration],
    ): ConfigWriter[OffsetTime @@ Tag] =
      enc.asInstanceOf[ConfigWriter[OffsetTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Duration],
    ): ConfigReader[OffsetTime @@ Tag] =
      dec.asInstanceOf[ConfigReader[OffsetTime @@ Tag]]

    implicit final def jtPeriodPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[Period],
    ): ConfigWriter[Period @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Period @@ Tag]]

    implicit final def jtPeriodPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[Period],
    ): ConfigReader[Period @@ Tag] =
      dec.asInstanceOf[ConfigReader[Period @@ Tag]]

    implicit final def jtYearPhantomTypeConfigWriter[Tag](implicit enc: ConfigWriter[Year]): ConfigWriter[Year @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Year @@ Tag]]

    implicit final def jtYearMonthPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[YearMonth],
    ): ConfigReader[YearMonth @@ Tag] =
      dec.asInstanceOf[ConfigReader[YearMonth @@ Tag]]

    implicit final def jtZonedDateTimePhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[ZonedDateTime],
    ): ConfigWriter[ZonedDateTime @@ Tag] =
      enc.asInstanceOf[ConfigWriter[ZonedDateTime @@ Tag]]

    implicit final def jtZoneIdPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[ZoneId],
    ): ConfigReader[ZoneId @@ Tag] =
      dec.asInstanceOf[ConfigReader[ZoneId @@ Tag]]

    implicit final def jtZoneOffsetPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[ZoneOffset],
    ): ConfigWriter[ZoneOffset @@ Tag] =
      enc.asInstanceOf[ConfigWriter[ZoneOffset @@ Tag]]

  }

  trait PhantomTypeTupleInstances {

    implicit final def tuple1PhantomTypeConfigWriter[T1, Tag](
      implicit enc: ConfigWriter[Tuple1[T1]],
    ): ConfigWriter[Tuple1[T1] @@ Tag] =
      enc.asInstanceOf[ConfigWriter[Tuple1[T1] @@ Tag]]

    implicit final def tuple1PhantomTypeConfigReader[T1, Tag](
      implicit dec: ConfigReader[Tuple1[T1]],
    ): ConfigReader[Tuple1[T1] @@ Tag] =
      dec.asInstanceOf[ConfigReader[Tuple1[T1] @@ Tag]]

    implicit final def tuple2PhantomTypeConfigWriter[T1, T2, Tag](
      implicit enc: ConfigWriter[(T1, T2)],
    ): ConfigWriter[(T1, T2) @@ Tag] =
      enc.asInstanceOf[ConfigWriter[(T1, T2) @@ Tag]]

    implicit final def tuple2PhantomTypeConfigReader[T1, T2, Tag](
      implicit dec: ConfigReader[(T1, T2)],
    ): ConfigReader[(T1, T2) @@ Tag] =
      dec.asInstanceOf[ConfigReader[(T1, T2) @@ Tag]]

    implicit final def tuple3PhantomTypeConfigWriter[T1, T2, T3, Tag](
      implicit enc: ConfigWriter[(T1, T2, T3)],
    ): ConfigWriter[(T1, T2, T3) @@ Tag] =
      enc.asInstanceOf[ConfigWriter[(T1, T2, T3) @@ Tag]]

    implicit final def tuple3PhantomTypeConfigReader[T1, T2, T3, Tag](
      implicit dec: ConfigReader[(T1, T2, T3)],
    ): ConfigReader[(T1, T2, T3) @@ Tag] =
      dec.asInstanceOf[ConfigReader[(T1, T2, T3) @@ Tag]]

    implicit final def tuple4PhantomTypeConfigWriter[T1, T2, T3, T4, Tag](
      implicit enc: ConfigWriter[(T1, T2, T3, T4)],
    ): ConfigWriter[(T1, T2, T3, T4) @@ Tag] =
      enc.asInstanceOf[ConfigWriter[(T1, T2, T3, T4) @@ Tag]]

    implicit final def tuple4PhantomTypeConfigReader[T1, T2, T3, T4, Tag](
      implicit dec: ConfigReader[(T1, T2, T3, T4)],
    ): ConfigReader[(T1, T2, T3, T4) @@ Tag] =
      dec.asInstanceOf[ConfigReader[(T1, T2, T3, T4) @@ Tag]]

    //TODO: all tuples
  }

  trait PhantomTypeJavaMiscInstances {
    import java.util.UUID

    implicit final def miscUUIDPhantomTypeConfigWriter[Tag](
      implicit enc: ConfigWriter[UUID],
    ): ConfigWriter[UUID @@ Tag] =
      enc.asInstanceOf[ConfigWriter[UUID @@ Tag]]

    implicit final def miscUUIDPhantomTypeConfigReader[Tag](
      implicit dec: ConfigReader[UUID],
    ): ConfigReader[UUID @@ Tag] =
      dec.asInstanceOf[ConfigReader[UUID @@ Tag]]

  }

}
