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
abstract class UnauthorizedAnomaly(
  override val message: String,
  causedBy:             Option[Throwable] = None,
) extends Anomaly(message, causedBy) with MeaningfulAnomalies.Unauthorized with Product with Serializable {
  override def id: AnomalyID = UnauthorizedAnomalyID
}

object UnauthorizedAnomaly
    extends UnauthorizedAnomaly(MeaningfulAnomalies.UnauthorizedMsg, None) with SingletonAnomalyProduct
    with AnomalyConstructors[UnauthorizedAnomaly] {

  override def apply(id: AnomalyID): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(id = id)

  override def apply(message: String): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(message = message)

  override def apply(parameters: Parameters): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(parameters = parameters)

  override def apply(id: AnomalyID, message: String): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(id = id, message = message)

  override def apply(id: AnomalyID, parameters: Parameters): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(id = id, parameters = parameters)

  override def apply(message: String, parameters: Parameters): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(message = message, parameters = parameters)

  override def apply(id: AnomalyID, message: String, parameters: Parameters): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(id = id, message = message, parameters = parameters)

  override def apply(a: Anomaly): UnauthorizedAnomaly =
    UnauthorizedAnomalyImpl(id = a.id, message = a.message, parameters = a.parameters)

}

final private[pureharm] case class UnauthorizedAnomalyImpl(
  override val id:         AnomalyID         = UnauthorizedAnomalyID,
  override val message:    String            = MeaningfulAnomalies.UnauthorizedMsg,
  override val parameters: Parameters        = Parameters.empty,
  causedBy:                Option[Throwable] = None,
) extends UnauthorizedAnomaly(message, causedBy) with Product with Serializable