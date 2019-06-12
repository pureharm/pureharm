package busymachines.pureharm.json_impl

import io.circe.{Decoder, Encoder}
import shapeless.tag.@@

/**
  *
  * Unfortunately type inference rarely (if ever) works
  * with something fully generic like
  * {{{
  *     trait LowPriorityPhantomTypeInstances {
  *
  *     implicit def genericPhantomTypeEncoder[Tag, T](implicit enc: Encoder[T]): Encoder[T @@ Tag] =
  *       enc.asInstanceOf[Encoder[T @@ Tag]]
  *
  *     implicit def genericPhantomTypeDecoder[Tag, T](implicit dec: Decoder[T]): Decoder[T @@ Tag] =
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

    implicit def stringPhantomTypeEncoder[Tag]: Encoder[String @@ Tag] =
      Encoder[String].asInstanceOf[Encoder[String @@ Tag]]

    implicit def stringPhantomTypeDecoder[Tag]: Decoder[String @@ Tag] =
      Decoder[String].asInstanceOf[Decoder[String @@ Tag]]

    implicit def booleanPhantomTypeEncoder[Tag]: Encoder[Boolean @@ Tag] =
      Encoder[Boolean].asInstanceOf[Encoder[Boolean @@ Tag]]

    implicit def booleanPhantomTypeDecoder[Tag]: Decoder[Boolean @@ Tag] =
      Decoder[Boolean].asInstanceOf[Decoder[Boolean @@ Tag]]

    implicit def bytePhantomTypeEncoder[Tag]: Encoder[Byte @@ Tag] =
      Encoder[Byte].asInstanceOf[Encoder[Byte @@ Tag]]

    implicit def bytePhantomTypeDecoder[Tag]: Decoder[Byte @@ Tag] =
      Decoder[Byte].asInstanceOf[Decoder[Byte @@ Tag]]

    implicit def shortPhantomTypeEncoder[Tag]: Encoder[Short @@ Tag] =
      Encoder[Short].asInstanceOf[Encoder[Short @@ Tag]]

    implicit def shortPhantomTypeDecoder[Tag]: Decoder[Short @@ Tag] =
      Decoder[Short].asInstanceOf[Decoder[Short @@ Tag]]

    implicit def charPhantomTypeEncoder[Tag]: Encoder[Char @@ Tag] =
      Encoder[Char].asInstanceOf[Encoder[Char @@ Tag]]

    implicit def charPhantomTypeDecoder[Tag]: Decoder[Char @@ Tag] =
      Decoder[Char].asInstanceOf[Decoder[Char @@ Tag]]

    implicit def intPhantomTypeEncoder[Tag]: Encoder[Int @@ Tag] =
      Encoder[Int].asInstanceOf[Encoder[Int @@ Tag]]

    implicit def intPhantomTypeDecoder[Tag]: Decoder[Int @@ Tag] =
      Decoder[Int].asInstanceOf[Decoder[Int @@ Tag]]

    implicit def longPhantomTypeEncoder[Tag]: Encoder[Long @@ Tag] =
      Encoder[Long].asInstanceOf[Encoder[Long @@ Tag]]

    implicit def longPhantomTypeDecoder[Tag]: Decoder[Long @@ Tag] =
      Decoder[Long].asInstanceOf[Decoder[Long @@ Tag]]

    implicit def floatPhantomTypeEncoder[Tag]: Encoder[Float @@ Tag] =
      Encoder[Float].asInstanceOf[Encoder[Float @@ Tag]]

    implicit def floatPhantomTypeDecoder[Tag]: Decoder[Float @@ Tag] =
      Decoder[Double].asInstanceOf[Decoder[Float @@ Tag]]

    implicit def doublePhantomTypeEncoder[Tag]: Encoder[Double @@ Tag] =
      Encoder[Double].asInstanceOf[Encoder[Double @@ Tag]]

    implicit def doublePhantomTypeDecoder[Tag]: Decoder[Double @@ Tag] =
      Decoder[Double].asInstanceOf[Decoder[Double @@ Tag]]

    implicit def bigDecimalPhantomTypeEncoder[Tag]: Encoder[BigDecimal @@ Tag] =
      Encoder[BigDecimal].asInstanceOf[Encoder[BigDecimal @@ Tag]]

    implicit def bigDecimalPhantomTypeDecoder[Tag]: Decoder[BigDecimal @@ Tag] =
      Decoder[BigDecimal].asInstanceOf[Decoder[BigDecimal @@ Tag]]

    implicit def bigIntPhantomTypeEncoder[Tag]: Encoder[BigInt @@ Tag] =
      Encoder[BigInt].asInstanceOf[Encoder[BigInt @@ Tag]]

    implicit def bigIntPhantomTypeDecoder[Tag]: Decoder[BigInt @@ Tag] =
      Decoder[BigInt].asInstanceOf[Decoder[BigInt @@ Tag]]
  }

  trait PhantomTypeCollectionInstances {
    import cats.data.{NonEmptyList, NonEmptyMap, NonEmptySet}

    implicit def phantomTypeListEncoder[Tag, T](implicit ls: Encoder[List[T]]): Encoder[List[T] @@ Tag] =
      ls.asInstanceOf[Encoder[List[T] @@ Tag]]

    implicit def phantomTypeListDecoder[Tag, T](implicit ls: Decoder[List[T]]): Decoder[List[T] @@ Tag] =
      ls.asInstanceOf[Decoder[List[T] @@ Tag]]

    implicit def phantomTypeNEListEncoder[Tag, T](
      implicit ls: Encoder[NonEmptyList[T]],
    ): Encoder[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptyList[T] @@ Tag]]

    implicit def phantomTypeNEListDecoder[Tag, T](
      implicit ls: Decoder[NonEmptyList[T]],
    ): Decoder[NonEmptyList[T] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptyList[T] @@ Tag]]

    implicit def phantomTypeSetEncoder[Tag, T](implicit ls: Encoder[Set[T]]): Encoder[Set[T] @@ Tag] =
      ls.asInstanceOf[Encoder[Set[T] @@ Tag]]

    implicit def phantomTypeSetDecoder[Tag, T](implicit ls: Decoder[Set[T]]): Decoder[Set[T] @@ Tag] =
      ls.asInstanceOf[Decoder[Set[T] @@ Tag]]

    implicit def phantomTypeNESetEncoder[Tag, T](
      implicit ls: Encoder[NonEmptySet[T]],
    ): Encoder[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptySet[T] @@ Tag]]

    implicit def phantomTypeNESetDecoder[Tag, T](
      implicit ls: Decoder[NonEmptySet[T]],
    ): Decoder[NonEmptySet[T] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptySet[T] @@ Tag]]

    implicit def phantomTypeMapEncoder[Tag, K, V](implicit ls: Encoder[Map[K, V]]): Encoder[Map[K, V] @@ Tag] =
      ls.asInstanceOf[Encoder[Map[K, V] @@ Tag]]

    implicit def phantomTypeMapDecoder[Tag, K, V](implicit ls: Decoder[Map[K, V]]): Decoder[Map[K, V] @@ Tag] =
      ls.asInstanceOf[Decoder[Map[K, V] @@ Tag]]

    implicit def phantomTypeNEMapEncoder[Tag, K, V](
      implicit ls: Encoder[NonEmptyMap[K, V]],
    ): Encoder[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[Encoder[NonEmptyMap[K, V] @@ Tag]]

    implicit def phantomTypeNEMapDecoder[Tag, K, V](
      implicit ls: Decoder[NonEmptyMap[K, V]],
    ): Decoder[NonEmptyMap[K, V] @@ Tag] =
      ls.asInstanceOf[Decoder[NonEmptyMap[K, V] @@ Tag]]
  }

  trait PhantomTypeScalaDurationInstances {
    import scala.concurrent.duration._

    implicit def sdDurationPhantomTypeEncoder[Tag](implicit enc: Encoder[Duration]): Encoder[Duration @@ Tag] =
      enc.asInstanceOf[Encoder[Duration @@ Tag]]

    implicit def sdDurationPhantomTypeDecoder[Tag](implicit dec: Decoder[Duration]): Decoder[Duration @@ Tag] =
      dec.asInstanceOf[Decoder[Duration @@ Tag]]

    implicit def sdFiniteDurationPhantomTypeEncoder[Tag](
      implicit enc: Encoder[FiniteDuration],
    ): Encoder[FiniteDuration @@ Tag] =
      enc.asInstanceOf[Encoder[FiniteDuration @@ Tag]]

    implicit def sdFiniteDurationPhantomTypeDecoder[Tag](
      implicit dec: Decoder[FiniteDuration],
    ): Decoder[FiniteDuration @@ Tag] =
      dec.asInstanceOf[Decoder[FiniteDuration @@ Tag]]

    implicit def sdDeadlinePhantomTypeEncoder[Tag](implicit enc: Encoder[Deadline]): Encoder[Deadline @@ Tag] =
      enc.asInstanceOf[Encoder[Deadline @@ Tag]]

    implicit def sdDeadlinePhantomTypeDecoder[Tag](implicit dec: Decoder[Deadline]): Decoder[Deadline @@ Tag] =
      dec.asInstanceOf[Decoder[Deadline @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {
    import java.time._

    implicit def jtDurationPhantomTypeEncoder[Tag](implicit enc: Encoder[Duration]): Encoder[Duration @@ Tag] =
      enc.asInstanceOf[Encoder[Duration @@ Tag]]

    implicit def jtDurationPhantomTypeDecoder[Tag](implicit dec: Decoder[Duration]): Decoder[Duration @@ Tag] =
      dec.asInstanceOf[Decoder[Duration @@ Tag]]

    implicit def jtInstantPhantomTypeEncoder[Tag](implicit enc: Encoder[Instant]): Encoder[Instant @@ Tag] =
      enc.asInstanceOf[Encoder[Instant @@ Tag]]

    implicit def jtInstantPhantomTypeDecoder[Tag](implicit dec: Decoder[Instant]): Decoder[Instant @@ Tag] =
      dec.asInstanceOf[Decoder[Instant @@ Tag]]

    implicit def jtLocalDatePhantomTypeEncoder[Tag](implicit enc: Encoder[LocalDate]): Encoder[LocalDate @@ Tag] =
      enc.asInstanceOf[Encoder[LocalDate @@ Tag]]

    implicit def jtLocalDatePhantomTypeDecoder[Tag](implicit dec: Decoder[LocalDate]): Decoder[LocalDate @@ Tag] =
      dec.asInstanceOf[Decoder[LocalDate @@ Tag]]

    implicit def jtLocalDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[LocalDateTime],
    ): Encoder[LocalDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[LocalDateTime @@ Tag]]

    implicit def jtLocalDateTimePhantomTypeDecoder[Tag](
      implicit dec: Decoder[LocalDateTime],
    ): Decoder[LocalDateTime @@ Tag] =
      dec.asInstanceOf[Decoder[LocalDateTime @@ Tag]]

    implicit def jtLocalTimePhantomTypeEncoder[Tag](implicit enc: Encoder[LocalTime]): Encoder[LocalTime @@ Tag] =
      enc.asInstanceOf[Encoder[LocalTime @@ Tag]]

    implicit def jtLocalTimePhantomTypeDecoder[Tag](implicit dec: Decoder[LocalTime]): Decoder[LocalTime @@ Tag] =
      dec.asInstanceOf[Decoder[LocalTime @@ Tag]]

    implicit def jtMonthDayPhantomTypeEncoder[Tag](implicit enc: Encoder[MonthDay]): Encoder[MonthDay @@ Tag] =
      enc.asInstanceOf[Encoder[MonthDay @@ Tag]]

    implicit def jtMonthDayPhantomTypeDecoder[Tag](implicit dec: Decoder[MonthDay]): Decoder[MonthDay @@ Tag] =
      dec.asInstanceOf[Decoder[MonthDay @@ Tag]]

    implicit def jtOffsetDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[OffsetDateTime],
    ): Encoder[OffsetDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[OffsetDateTime @@ Tag]]

    implicit def jtOffsetDateTimePhantomTypeDecoder[Tag](
      implicit dec: Decoder[OffsetDateTime],
    ): Decoder[OffsetDateTime @@ Tag] =
      dec.asInstanceOf[Decoder[OffsetDateTime @@ Tag]]

    implicit def jtOffsetTimePhantomTypeEncoder[Tag](implicit enc: Encoder[Duration]): Encoder[OffsetTime @@ Tag] =
      enc.asInstanceOf[Encoder[OffsetTime @@ Tag]]

    implicit def jtOffsetTimePhantomTypeDecoder[Tag](implicit dec: Decoder[Duration]): Decoder[OffsetTime @@ Tag] =
      dec.asInstanceOf[Decoder[OffsetTime @@ Tag]]

    implicit def jtPeriodPhantomTypeEncoder[Tag](implicit enc: Encoder[Period]): Encoder[Period @@ Tag] =
      enc.asInstanceOf[Encoder[Period @@ Tag]]

    implicit def jtPeriodPhantomTypeDecoder[Tag](implicit dec: Decoder[Period]): Decoder[Period @@ Tag] =
      dec.asInstanceOf[Decoder[Period @@ Tag]]

    implicit def jtYearPhantomTypeEncoder[Tag](implicit enc: Encoder[Year]): Encoder[Year @@ Tag] =
      enc.asInstanceOf[Encoder[Year @@ Tag]]

    implicit def jtYearMonthPhantomTypeDecoder[Tag](implicit dec: Decoder[YearMonth]): Decoder[YearMonth @@ Tag] =
      dec.asInstanceOf[Decoder[YearMonth @@ Tag]]

    implicit def jtZonedDateTimePhantomTypeEncoder[Tag](
      implicit enc: Encoder[ZonedDateTime],
    ): Encoder[ZonedDateTime @@ Tag] =
      enc.asInstanceOf[Encoder[ZonedDateTime @@ Tag]]

    implicit def jtZoneIdPhantomTypeDecoder[Tag](implicit dec: Decoder[ZoneId]): Decoder[ZoneId @@ Tag] =
      dec.asInstanceOf[Decoder[ZoneId @@ Tag]]

    implicit def jtZoneOffsetPhantomTypeEncoder[Tag](implicit enc: Encoder[ZoneOffset]): Encoder[ZoneOffset @@ Tag] =
      enc.asInstanceOf[Encoder[ZoneOffset @@ Tag]]

  }

  trait PhantomTypeTupleInstances {
    implicit def tuple1PhantomTypeEncoder[T1, Tag](implicit enc: Encoder[Tuple1[T1]]): Encoder[Tuple1[T1] @@ Tag] =
      enc.asInstanceOf[Encoder[Tuple1[T1] @@ Tag]]

    implicit def tuple1PhantomTypeDecoder[T1, Tag](implicit dec: Decoder[Tuple1[T1]]): Decoder[Tuple1[T1] @@ Tag] =
      dec.asInstanceOf[Decoder[Tuple1[T1] @@ Tag]]

    implicit def tuple2PhantomTypeEncoder[T1, T2, Tag](
      implicit enc: Encoder[(T1, T2)],
    ): Encoder[(T1, T2) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2) @@ Tag]]

    implicit def tuple2PhantomTypeDecoder[T1, T2, Tag](
      implicit dec: Decoder[(T1, T2)],
    ): Decoder[(T1, T2) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2) @@ Tag]]

    implicit def tuple3PhantomTypeEncoder[T1, T2, T3, Tag](
      implicit enc: Encoder[(T1, T2, T3)],
    ): Encoder[(T1, T2, T3) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2, T3) @@ Tag]]

    implicit def tuple3PhantomTypeDecoder[T1, T2, T3, Tag](
      implicit dec: Decoder[(T1, T2, T3)],
    ): Decoder[(T1, T2, T3) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2, T3) @@ Tag]]

    implicit def tuple4PhantomTypeEncoder[T1, T2, T3, T4, Tag](
      implicit enc: Encoder[(T1, T2, T3, T4)],
    ): Encoder[(T1, T2, T3, T4) @@ Tag] =
      enc.asInstanceOf[Encoder[(T1, T2, T3, T4) @@ Tag]]

    implicit def tuple4PhantomTypeDecoder[T1, T2, T3, T4, Tag](
      implicit dec: Decoder[(T1, T2, T3, T4)],
    ): Decoder[(T1, T2, T3, T4) @@ Tag] =
      dec.asInstanceOf[Decoder[(T1, T2, T3, T4) @@ Tag]]

    //TODO: all tuples
  }

  trait PhantomTypeJavaMiscInstances {
    import java.util.UUID
    implicit def miscUUIDPhantomTypeEncoder[Tag](implicit enc: Encoder[UUID]): Encoder[UUID @@ Tag] =
      enc.asInstanceOf[Encoder[UUID @@ Tag]]

    implicit def miscUUIDPhantomTypeDecoder[Tag](implicit dec: Decoder[UUID]): Decoder[UUID @@ Tag] =
      dec.asInstanceOf[Decoder[UUID @@ Tag]]

  }

}
