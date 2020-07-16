package busymachines.pureharm.internals.rest

import busymachines.pureharm.anomaly.{AnomalyBase, SeqStringWrapper}
import sttp.tapir

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jul 2020
  */
object PureharmTapirSchemas {

  val tapirSchemaAnomalies: tapir.Schema[Throwable] = {
    import cats.data.NonEmptyChain
    import cats.implicits._
    val seqOrString: tapir.Schema[SeqStringWrapper] = tapir.Schema(
      tapir.SchemaType.SCoproduct(
        tapir.SchemaType.SObjectInfo("string or array of strings"),
        List(
          tapir.Schema.schemaForString,
          tapir.Schema.schemaForArray[String],
        ),
        None,
      )
    )

    //using Chain because adding things with symbolic operators to an Iterable is... is...
    val baseSchema = NonEmptyChain(
      "id"         -> tapir.Schema.schemaForString,
      "message"    -> tapir.Schema.schemaForString,
      "parameters" -> tapir.Schema.schemaForOption(
        tapir.Schema.schemaForMap[SeqStringWrapper](seqOrString)
      ),
    )

    val schemaAnomalyBase: tapir.Schema[AnomalyBase] = tapir.Schema(
      tapir.SchemaType.SProduct(
        tapir.SchemaType.SObjectInfo("io.circe.JsonObject"),
        baseSchema.toIterable,
      )
    )
    tapir.Schema(
      tapir.SchemaType.SProduct(
        tapir.SchemaType.SObjectInfo("io.circe.JsonObject"),
        baseSchema
          .append("messages" -> tapir.Schema.schemaForOption[AnomalyBase](schemaAnomalyBase))
          .toIterable,
      )
    )
  }

}
