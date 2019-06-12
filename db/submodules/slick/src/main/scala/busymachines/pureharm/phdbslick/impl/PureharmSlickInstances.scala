package busymachines.pureharm.phdbslick.impl

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

  trait PhantomTypeInstances extends PhantomTypePrimitiveInstances

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
}
