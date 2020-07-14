package busymachines.pureharm.rest

import java.util.UUID

import busymachines.pureharm.anomaly.{AnomalyBase, SeqStringWrapper}
import busymachines.pureharm.internals.rest.PureharmTapirSchemas
import busymachines.pureharm.phantom.Spook
import sttp.tapir

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object PureharmRestTapirImplicits extends PureharmRestTapirImplicits

trait PureharmRestTapirImplicits {

  @inline private def genericPhantomTypePathMatcher[Underlying, PT](implicit
    tc: tapir.Codec.PlainCodec[Underlying],
    p:  Spook[Underlying, PT],
  ): tapir.Codec.PlainCodec[PT] = tc.map(p.spook _)(p.despook)

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

  implicit def genericSchema[Underlying, PT: Spook[Underlying, *]](implicit
    sc: tapir.Schema[Underlying]
  ): tapir.Schema[PT] =
    sc.copy(description = sc.description match {
      case None           => Option(Spook[Underlying, PT].symbolicName)
      case Some(original) => Option(s"$original — type name: ${Spook[Underlying, PT].symbolicName}")
    })

  implicit def genericValidator[Underlying, PT: Spook[Underlying, *]](implicit
    sc: tapir.Validator[Underlying]
  ): tapir.Validator[PT] = sc.contramap(Spook[Underlying, PT].despook)

  /**
    * While it's a bit iffy that technically,
    * only AnomaliesBase contains the "messages"
    * fields... it is much easier to do this,
    * since it's technically true, furthermore,
    * this makes erroring out of the application
    * much easier
    */
  implicit val tapirSchemaAnomalies: tapir.Schema[Throwable] =
    PureharmTapirSchemas.tapirSchemaAnomalies
}
