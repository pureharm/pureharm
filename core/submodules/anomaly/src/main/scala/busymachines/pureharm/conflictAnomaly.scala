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
package busymachines.pureharm

import busymachines.pureharm.Anomaly.Parameters

/**
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 10 Jun 2019
  *
  */
abstract class ConflictAnomaly(
  override val message: String,
  causedBy:             Option[Throwable] = None,
) extends Anomaly(message, causedBy) with MeaningfulAnomalies.Conflict with Product with Serializable {
  override def id: AnomalyID = ConflictAnomalyID
}

object ConflictAnomaly
    extends ConflictAnomaly(MeaningfulAnomalies.ConflictMsg, None) with SingletonAnomalyProduct
    with AnomalyConstructors[ConflictAnomaly] {
  override def apply(id: AnomalyID): ConflictAnomaly =
    ConflictFailureImpl(id = id)

  override def apply(message: String): ConflictAnomaly =
    ConflictFailureImpl(message = message)

  override def apply(parameters: Parameters): ConflictAnomaly =
    ConflictFailureImpl(parameters = parameters)

  override def apply(id: AnomalyID, message: String): ConflictAnomaly =
    ConflictFailureImpl(id = id, message = message)

  override def apply(id: AnomalyID, parameters: Parameters): ConflictAnomaly =
    ConflictFailureImpl(id = id, parameters = parameters)

  override def apply(message: String, parameters: Parameters): ConflictAnomaly =
    ConflictFailureImpl(message = message, parameters = parameters)

  override def apply(id: AnomalyID, message: String, parameters: Parameters): ConflictAnomaly =
    ConflictFailureImpl(id = id, message = message, parameters = parameters)

  override def apply(a: Anomaly): ConflictAnomaly =
    ConflictFailureImpl(id = a.id, message = a.message, parameters = a.parameters)
}

final private[pureharm] case class ConflictFailureImpl(
  override val id:         AnomalyID         = ConflictAnomalyID,
  override val message:    String            = MeaningfulAnomalies.ConflictMsg,
  override val parameters: Parameters        = Parameters.empty,
  causedBy:                Option[Throwable] = None,
) extends ConflictAnomaly(message, causedBy) with Product with Serializable
