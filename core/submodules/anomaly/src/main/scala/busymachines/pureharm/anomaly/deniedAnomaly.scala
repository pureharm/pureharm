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

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
abstract class DeniedAnomaly(
  override val message:  String,
  override val causedBy: Option[Throwable] = None,
) extends Anomaly(message, causedBy) with MeaningfulAnomalies.Denied with Product with Serializable {
  override def id: AnomalyID = DeniedAnomalyID
}

object DeniedAnomaly
  extends DeniedAnomaly(MeaningfulAnomalies.DeniedMsg, None) with SingletonAnomalyProduct
  with AnomalyConstructors[DeniedAnomaly] {

  override def apply(id:         AnomalyID):            DeniedAnomaly       =
    DeniedAnomalyImpl(id = id)

  override def apply(message:    String):               DeniedAnomaly       =
    DeniedAnomalyImpl(message = message)

  override def apply(parameters: Anomaly.Parameters): DeniedAnomaly =
    DeniedAnomalyImpl(params = parameters)

  override def apply(id:         AnomalyID, message: String): DeniedAnomaly =
    DeniedAnomalyImpl(id = id, message = message)

  override def apply(id:         AnomalyID, parameters: Anomaly.Parameters): DeniedAnomaly =
    DeniedAnomalyImpl(id = id, params = parameters)

  override def apply(message:    String, parameters:    Anomaly.Parameters): DeniedAnomaly =
    DeniedAnomalyImpl(message = message, params = parameters)

  override def apply(id:         AnomalyID, message: String, parameters: Anomaly.Parameters): DeniedAnomaly =
    DeniedAnomalyImpl(id = id, message = message, params = parameters)

  override def apply(a:          AnomalyBase): DeniedAnomaly =
    DeniedAnomalyImpl(id = a.id, message = a.message, params = a.parameters)
}

final private[pureharm] case class DeniedAnomalyImpl(
  override val id:       AnomalyID = DeniedAnomalyID,
  override val message:  String = MeaningfulAnomalies.DeniedMsg,
  params:                Anomaly.Parameters = Anomaly.Parameters.empty,
  override val causedBy: Option[Throwable] = None,
) extends DeniedAnomaly(message, causedBy) with Product with Serializable {
  override val parameters: Anomaly.Parameters = super.parameters ++ params
}
