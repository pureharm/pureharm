/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.anomaly

import scala.collection.immutable.Seq

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
trait AnomaliesBase extends AnomalyBase with Product with Serializable {
  def firstAnomaly: AnomalyBase

  def restOfAnomalies: Seq[AnomalyBase]

  final def messages: Seq[AnomalyBase] =
    firstAnomaly +: restOfAnomalies
}

abstract class Anomalies(
  override val id:              AnomalyID,
  override val message:         String,
  override val firstAnomaly:    Anomaly,
  override val restOfAnomalies: Seq[Anomaly],
) extends Exception(message) with AnomaliesBase with Product with Serializable {}

object Anomalies {

  def apply(id: AnomalyID, message: String, msg: Anomaly, msgs: Anomaly*): Anomalies =
    AnomaliesImpl(id, message, msg, msgs.toList)
}

final private[pureharm] case class AnomaliesImpl(
  override val id:              AnomalyID,
  override val message:         String,
  override val firstAnomaly:    Anomaly,
  override val restOfAnomalies: Seq[Anomaly],
) extends Anomalies(id, message, firstAnomaly, restOfAnomalies) with Product with Serializable
