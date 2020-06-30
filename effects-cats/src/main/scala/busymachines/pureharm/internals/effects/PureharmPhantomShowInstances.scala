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
package busymachines.pureharm.internals.effects

import busymachines.pureharm.effects.Show
import shapeless.tag.@@

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
object PureharmPhantomShowInstances {

  trait Implicits extends PhantomTypeInstances

  trait PhantomTypeInstances
    extends PhantomTypePrimitiveInstances with PhantomTypeCollectionInstances with PhantomTypeScalaDurationInstances
    with PhantomTypeJavaTimeInstances with PhantomTypeTupleInstances with PhantomTypeJavaMiscInstances

  trait PhantomTypePrimitiveInstances {

    implicit final def stringPhantomTypeShow[Tag](implicit sh: Show[String]): Show[String @@ Tag] =
      sh.asInstanceOf[Show[String @@ Tag]]

    implicit final def booleanPhantomTypeShow[Tag](implicit sh: Show[Boolean]): Show[Boolean @@ Tag] =
      sh.asInstanceOf[Show[Boolean @@ Tag]]

    implicit final def bytePhantomTypeShow[Tag](implicit sh: Show[Byte]): Show[Byte @@ Tag] =
      sh.asInstanceOf[Show[Byte @@ Tag]]

    implicit final def shortPhantomTypeShow[Tag](implicit sh: Show[Short]): Show[Short @@ Tag] =
      sh.asInstanceOf[Show[Short @@ Tag]]

    implicit final def charPhantomTypeShow[Tag](implicit sh: Show[Char]): Show[Char @@ Tag] =
      Show[Char].asInstanceOf[Show[Char @@ Tag]]

    implicit final def intPhantomTypeShow[Tag](implicit sh: Show[Int]): Show[Int @@ Tag] =
      sh.asInstanceOf[Show[Int @@ Tag]]

    implicit final def longPhantomTypeShow[Tag](implicit sh: Show[Long]): Show[Long @@ Tag] =
      sh.asInstanceOf[Show[Long @@ Tag]]

    implicit final def floatPhantomTypeShow[Tag](implicit sh: Show[Float]): Show[Float @@ Tag] =
      sh.asInstanceOf[Show[Float @@ Tag]]

    implicit final def doublePhantomTypeShow[Tag](implicit sh: Show[Double]): Show[Double @@ Tag] =
      Show[Double].asInstanceOf[Show[Double @@ Tag]]

    implicit final def bigDecimalPhantomTypeShow[Tag](implicit sh: Show[BigDecimal]): Show[BigDecimal @@ Tag] =
      sh.asInstanceOf[Show[BigDecimal @@ Tag]]

    implicit final def bigIntPhantomTypeShow[Tag](implicit sh: Show[BigInt]): Show[BigInt @@ Tag] =
      sh.asInstanceOf[Show[BigInt @@ Tag]]

  }

  trait PhantomTypeCollectionInstances {
    import cats.data.{NonEmptyList, NonEmptyMap, NonEmptySet}

    implicit final def phantomTypeListShow[Tag, T](implicit
      ls: Show[List[T]]
    ): Show[List[T] @@ Tag] =
      ls.asInstanceOf[Show[List[T] @@ Tag]]

    implicit final def phantomTypeNEListShow[Tag, T](implicit
      ls: Show[NonEmptyList[T]]
    ): Show[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[Show[NonEmptyList[T] @@ Tag]]

    implicit final def phantomTypeSetShow[Tag, T](implicit
      ls: Show[Set[T]]
    ): Show[Set[T] @@ Tag] =
      ls.asInstanceOf[Show[Set[T] @@ Tag]]

    implicit final def phantomTypeNESetShow[Tag, T](implicit
      ls: Show[NonEmptySet[T]]
    ): Show[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[Show[NonEmptySet[T] @@ Tag]]

    implicit final def phantomTypeMapShow[Tag, K, V](implicit
      ls: Show[Map[K, V]]
    ): Show[Map[K, V] @@ Tag] =
      ls.asInstanceOf[Show[Map[K, V] @@ Tag]]

    implicit final def phantomTypeNEMapShow[Tag, K, V](implicit
      ls: Show[NonEmptyMap[K, V]]
    ): Show[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[Show[NonEmptyMap[K, V] @@ Tag]]
  }

  trait PhantomTypeScalaDurationInstances {
    import scala.concurrent.duration._

    implicit final def sdDurationPhantomTypeShow[Tag](implicit
      sh: Show[Duration]
    ): Show[Duration @@ Tag] =
      sh.asInstanceOf[Show[Duration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeShow[Tag](implicit
      sh: Show[FiniteDuration]
    ): Show[FiniteDuration @@ Tag] =
      sh.asInstanceOf[Show[FiniteDuration @@ Tag]]

    implicit final def sdDeadlinePhantomTypeShow[Tag](implicit
      sh: Show[Deadline]
    ): Show[Deadline @@ Tag] =
      sh.asInstanceOf[Show[Deadline @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {
    import java.time._

    implicit final def jtDurationPhantomTypeShow[Tag](implicit
      sh: Show[Duration]
    ): Show[Duration @@ Tag] =
      sh.asInstanceOf[Show[Duration @@ Tag]]

    implicit final def jtInstantPhantomTypeShow[Tag](implicit
      sh: Show[Instant]
    ): Show[Instant @@ Tag] =
      sh.asInstanceOf[Show[Instant @@ Tag]]

    implicit final def jtLocalDatePhantomTypeShow[Tag](implicit
      sh: Show[LocalDate]
    ): Show[LocalDate @@ Tag] =
      sh.asInstanceOf[Show[LocalDate @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeShow[Tag](implicit
      sh: Show[LocalDateTime]
    ): Show[LocalDateTime @@ Tag] =
      sh.asInstanceOf[Show[LocalDateTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeShow[Tag](implicit
      sh: Show[LocalTime]
    ): Show[LocalTime @@ Tag] =
      sh.asInstanceOf[Show[LocalTime @@ Tag]]

    implicit final def jtMonthDayPhantomTypeShow[Tag](implicit
      sh: Show[MonthDay]
    ): Show[MonthDay @@ Tag] =
      sh.asInstanceOf[Show[MonthDay @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeShow[Tag](implicit
      sh: Show[OffsetDateTime]
    ): Show[OffsetDateTime @@ Tag] =
      sh.asInstanceOf[Show[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeShow[Tag](implicit
      sh: Show[Duration]
    ): Show[OffsetTime @@ Tag] =
      sh.asInstanceOf[Show[OffsetTime @@ Tag]]

    implicit final def jtPeriodPhantomTypeShow[Tag](implicit
      sh: Show[Period]
    ): Show[Period @@ Tag] =
      sh.asInstanceOf[Show[Period @@ Tag]]

    implicit final def jtYearPhantomTypeShow[Tag](implicit sh: Show[Year]): Show[Year @@ Tag] =
      sh.asInstanceOf[Show[Year @@ Tag]]

    implicit final def jtZonedDateTimePhantomTypeShow[Tag](implicit
      sh: Show[ZonedDateTime]
    ): Show[ZonedDateTime @@ Tag] =
      sh.asInstanceOf[Show[ZonedDateTime @@ Tag]]

    implicit final def jtZoneOffsetPhantomTypeShow[Tag](implicit
      sh: Show[ZoneOffset]
    ): Show[ZoneOffset @@ Tag] =
      sh.asInstanceOf[Show[ZoneOffset @@ Tag]]

  }

  trait PhantomTypeTupleInstances {

    implicit final def tuple1PhantomTypeShow[T1, Tag](implicit
      sh: Show[Tuple1[T1]]
    ): Show[Tuple1[T1] @@ Tag] =
      sh.asInstanceOf[Show[Tuple1[T1] @@ Tag]]

    implicit final def tuple2PhantomTypeShow[T1, T2, Tag](implicit
      sh: Show[(T1, T2)]
    ): Show[(T1, T2) @@ Tag] =
      sh.asInstanceOf[Show[(T1, T2) @@ Tag]]

    implicit final def tuple3PhantomTypeShow[T1, T2, T3, Tag](implicit
      sh: Show[(T1, T2, T3)]
    ): Show[(T1, T2, T3) @@ Tag] =
      sh.asInstanceOf[Show[(T1, T2, T3) @@ Tag]]

    implicit final def tuple4PhantomTypeShow[T1, T2, T3, T4, Tag](implicit
      sh: Show[(T1, T2, T3, T4)]
    ): Show[(T1, T2, T3, T4) @@ Tag] =
      sh.asInstanceOf[Show[(T1, T2, T3, T4) @@ Tag]]

    //TODO: all tuples
  }

  trait PhantomTypeJavaMiscInstances {
    import java.util.UUID

    implicit final def miscUUIDPhantomTypeShow[Tag](implicit
      sh: Show[UUID]
    ): Show[UUID @@ Tag] =
      sh.asInstanceOf[Show[UUID @@ Tag]]

  }

}
