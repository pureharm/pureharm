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
package busymachines.pureharm.internals.dbslick

import shapeless.tag.@@

/**
  *
  * Unfortunately type inference rarely (if ever) works
  * with something fully generic like
  * {{{
  *     trait LowPriorityPhantomTypeInstances {
  *     final implicit def genericColumnType[Tag, T](implicit ct: ColumnType[T]): ColumnType[T @@ Tag] =
  *       ct.asInstanceOf[ColumnType[T @@ Tag]]
  *   }
  * }}}
  *
  * At some point in time another crack will have to be taken to implement
  * the fully generic version, but for now giving up...
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
object PureharmSlickInstances {

  trait PhantomTypeInstances
      extends PhantomTypePrimitiveInstances with PhantomTypeScalaDurationInstances with PhantomTypeJavaTimeInstances
      with PhantomTypeJavaMiscInstances

  trait PhantomTypePrimitiveInstances {
    protected val enclosingProfile: slick.jdbc.JdbcProfile

    import enclosingProfile._

    implicit final def stringPhantomTypeColumnType[Tag]: ColumnType[String @@ Tag] =
      api.stringColumnType.asInstanceOf[ColumnType[String @@ Tag]]

    implicit final def booleanPhantomTypeColumnType[Tag]: ColumnType[Boolean @@ Tag] =
      api.booleanColumnType.asInstanceOf[ColumnType[Boolean @@ Tag]]

    implicit final def bytePhantomTypeColumnType[Tag]: ColumnType[Byte @@ Tag] =
      api.byteColumnType.asInstanceOf[ColumnType[Byte @@ Tag]]

    implicit final def shortPhantomTypeColumnType[Tag]: ColumnType[Short @@ Tag] =
      api.shortColumnType.asInstanceOf[ColumnType[Short @@ Tag]]

    implicit final def charPhantomTypeColumnType[Tag]: ColumnType[Char @@ Tag] =
      api.charColumnType.asInstanceOf[ColumnType[Char @@ Tag]]

    implicit final def intPhantomTypeColumnType[Tag]: ColumnType[Int @@ Tag] =
      api.intColumnType.asInstanceOf[ColumnType[Int @@ Tag]]

    implicit final def longPhantomTypeColumnType[Tag]: ColumnType[Long @@ Tag] =
      api.longColumnType.asInstanceOf[ColumnType[Long @@ Tag]]

    implicit final def floatPhantomTypeColumnType[Tag]: ColumnType[Float @@ Tag] =
      api.floatColumnType.asInstanceOf[ColumnType[Float @@ Tag]]

    implicit final def doublePhantomTypeColumnType[Tag]: ColumnType[Double @@ Tag] =
      api.doubleColumnType.asInstanceOf[ColumnType[Double @@ Tag]]

    implicit final def bigDecimalPhantomTypeColumnType[Tag]: ColumnType[BigDecimal @@ Tag] =
      api.bigDecimalColumnType.asInstanceOf[ColumnType[BigDecimal @@ Tag]]

    implicit final def bigIntPhantomTypeColumnType[Tag](implicit ct: ColumnType[BigInt]): ColumnType[BigInt @@ Tag] =
      ct.asInstanceOf[ColumnType[BigInt @@ Tag]]
  }

  trait PhantomTypeScalaDurationInstances {
    protected val enclosingProfile: slick.jdbc.JdbcProfile

    import enclosingProfile._

    import scala.concurrent.duration._

    implicit final def sdDurationPhantomTypeColumnType[Tag](
      implicit ct: ColumnType[Duration],
    ): ColumnType[Duration @@ Tag] =
      ct.asInstanceOf[ColumnType[Duration @@ Tag]]

    implicit final def sdFiniteDurationPhantomTypeColumnType[Tag](
      implicit ct: ColumnType[FiniteDuration],
    ): ColumnType[FiniteDuration @@ Tag] =
      ct.asInstanceOf[ColumnType[FiniteDuration @@ Tag]]

    implicit final def sdDeadlinePhantomTypeColumnType[Tag](
      implicit ct: ColumnType[Deadline],
    ): ColumnType[Deadline @@ Tag] =
      ct.asInstanceOf[ColumnType[Deadline @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {
    protected val enclosingProfile: slick.jdbc.JdbcProfile

    import java.time._

    import enclosingProfile._

    implicit final def jtDurationPhantomTypeColumnType[Tag](
      implicit enc: ColumnType[Duration],
    ): ColumnType[Duration @@ Tag] =
      enc.asInstanceOf[ColumnType[Duration @@ Tag]]

    implicit final def jtInstantPhantomTypeColumnType[Tag](
      implicit enc: ColumnType[Instant],
    ): ColumnType[Instant @@ Tag] =
      enc.asInstanceOf[ColumnType[Instant @@ Tag]]

    implicit final def jtLocalDatePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[LocalDate],
    ): ColumnType[LocalDate @@ Tag] =
      enc.asInstanceOf[ColumnType[LocalDate @@ Tag]]

    implicit final def jtLocalDateTimePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[LocalDateTime],
    ): ColumnType[LocalDateTime @@ Tag] =
      enc.asInstanceOf[ColumnType[LocalDateTime @@ Tag]]

    implicit final def jtLocalTimePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[LocalTime],
    ): ColumnType[LocalTime @@ Tag] =
      enc.asInstanceOf[ColumnType[LocalTime @@ Tag]]

    implicit final def jtMonthDayPhantomTypeColumnType[Tag](
      implicit enc: ColumnType[MonthDay],
    ): ColumnType[MonthDay @@ Tag] =
      enc.asInstanceOf[ColumnType[MonthDay @@ Tag]]

    implicit final def jtOffsetDateTimePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[OffsetDateTime],
    ): ColumnType[OffsetDateTime @@ Tag] =
      enc.asInstanceOf[ColumnType[OffsetDateTime @@ Tag]]

    implicit final def jtOffsetTimePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[Duration],
    ): ColumnType[OffsetTime @@ Tag] =
      enc.asInstanceOf[ColumnType[OffsetTime @@ Tag]]

    implicit final def jtPeriodPhantomTypeColumnType[Tag](implicit enc: ColumnType[Period]): ColumnType[Period @@ Tag] =
      enc.asInstanceOf[ColumnType[Period @@ Tag]]

    implicit final def jtYearPhantomTypeColumnType[Tag](implicit enc: ColumnType[Year]): ColumnType[Year @@ Tag] =
      enc.asInstanceOf[ColumnType[Year @@ Tag]]

    implicit final def jtZonedDateTimePhantomTypeColumnType[Tag](
      implicit enc: ColumnType[ZonedDateTime],
    ): ColumnType[ZonedDateTime @@ Tag] =
      enc.asInstanceOf[ColumnType[ZonedDateTime @@ Tag]]

    implicit final def jtZoneOffsetPhantomTypeColumnType[Tag](
      implicit enc: ColumnType[ZoneOffset],
    ): ColumnType[ZoneOffset @@ Tag] =
      enc.asInstanceOf[ColumnType[ZoneOffset @@ Tag]]
  }

  trait PhantomTypeJavaMiscInstances {
    protected val enclosingProfile: slick.jdbc.JdbcProfile
    import java.util.UUID

    import enclosingProfile._

    implicit final def miscUUIDPhantomTypeColumnType[Tag](implicit enc: ColumnType[UUID]): ColumnType[UUID @@ Tag] =
      enc.asInstanceOf[ColumnType[UUID @@ Tag]]

  }
}
