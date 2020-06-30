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
package busymachines.pureharm.json

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
trait DefaultTypeDiscriminatorConfig {

  /**
    * This exists to give us the default behavior of deserializing sealed trait
    * hierarchies by adding and "_type" field to the json, instead of creating
    * a property for each variant.
    *
    * Unfortunately, this uses the name of each variant as the value for the
    * "_type" field, leaving JSON-value APIs vulnerable to rename refactorings.
    */
  implicit final val defaultDerivationConfiguration: io.circe.generic.extras.Configuration =
    io.circe.generic.extras.Configuration.default
      .withDiscriminator(DefaultTypeDiscriminatorConfig.JsonTypeString)

}

object DefaultTypeDiscriminatorConfig {
  final val JsonTypeString: String = "_type"
}
