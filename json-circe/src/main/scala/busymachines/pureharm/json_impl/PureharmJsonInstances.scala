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
      extends PhantomTypePrimitiveInstances with PhantomTypeCollectionInstances with PhantomTypeJavaTimeInstances

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
    implicit def phantomTypeListEncoder[Tag, T](implicit ls: Encoder[List[T]]): Encoder[List[T] @@ Tag] =
      ls.asInstanceOf[Encoder[List[T] @@ Tag]]

    implicit def phantomTypeListDecoder[Tag, T](implicit ls: Decoder[List[T]]): Decoder[List[T] @@ Tag] =
      ls.asInstanceOf[Decoder[List[T] @@ Tag]]
  }

  trait PhantomTypeJavaTimeInstances {}

  trait PhantomTypeJavaMiscInstances {}

}
