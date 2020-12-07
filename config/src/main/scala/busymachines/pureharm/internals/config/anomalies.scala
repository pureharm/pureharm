/** Copyright (c) 2019 BusyMachines
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

import busymachines.pureharm.anomaly._
import pureconfig.error.{ConfigReaderFailure, ConfigReaderFailures}

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
final case class ConfigReadingAnomaly(c: ConfigReaderFailure)
  extends InvalidInputAnomaly(s"Failed to read config because: ${c.description}") {

  override val id: AnomalyID = ConfigReadingAnomalyID

  override val parameters: Anomaly.Parameters = {
    val orig: Anomaly.Parameters = super.parameters ++ Anomaly.Parameters(
      "reason" -> c.description
    )
    val loc = c.origin.map(l => "location" -> l.description: (String, Anomaly.Parameter))
    orig.++(loc.toMap: Anomaly.Parameters)
  }
}

final case class ConfigAggregateAnomalies(cs: ConfigReaderFailures, namespace: Option[String] = None)
  extends Anomaly(
    message =
      s"Failed to read config file. ${cs.toList.map(_.description).mkString(",")} >>> namespace: ${namespace.getOrElse("unknown")}"
  ) with AnomaliesBase {
  override val id: AnomalyID = ConfigAggregateAnomaliesID

  override val firstAnomaly:    ConfigReadingAnomaly       = ConfigReadingAnomaly(cs.head)
  override val restOfAnomalies: List[ConfigReadingAnomaly] = cs.tail.map(ConfigReadingAnomaly.apply).toList

  def withNamespace(ns: String): ConfigAggregateAnomalies = this.copy(namespace = Option(ns))
}

final case class ConfigSourceLoadingAnomaly(cause: Throwable)
  extends InvalidInputAnomaly(s"Failed to load config because: ${cause.toString}", causedBy = Option(cause)) {

  override val id: AnomalyID = ConfigSourceLoadingAnomalyID

  override val parameters: Anomaly.Parameters = {
    super.parameters ++ Anomaly.Parameters(
      "stackTrace" -> cause.getStackTrace.map(_.toString).toSeq
    )
  }
}

//----------------------------- config ----------------------------
case object ConfigReadingAnomalyID       extends AnomalyID { override val name: String = "ph-config-001" }
case object ConfigAggregateAnomaliesID   extends AnomalyID { override val name: String = "ph-config-002" }
case object ConfigSourceLoadingAnomalyID extends AnomalyID { override val name: String = "ph-config-003" }
