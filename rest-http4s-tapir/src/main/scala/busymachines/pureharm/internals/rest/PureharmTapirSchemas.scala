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
