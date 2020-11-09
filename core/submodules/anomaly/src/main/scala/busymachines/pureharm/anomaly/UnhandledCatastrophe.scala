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

import busymachines.pureharm.anomaly.Anomaly.Parameters

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jul 2020
  */
abstract class UnhandledCatastrophe(
  override val message: String,
  cause:                Throwable,
  params:               Anomaly.Parameters,
) extends Catastrophe(message, Option(cause)) with Product with Serializable {
  override def id: AnomalyID = UnhandledCatastropheID

  override def parameters: Parameters = params ++ Parameters(
    "causedBy" -> cause.toString
  )
}

private[pureharm] case object UnhandledCatastropheID extends AnomalyID with Product with Serializable {
  override val name: String = "UH_0"
}

object UnhandledCatastrophe {
  private[pureharm] val UnhandledCatastropheMsg: String = "Unhandled Catastrophe"

  def apply(causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(message = s"Unhandled throwable: ${causedBy.getMessage}", cause = causedBy)

  def apply(id:       AnomalyID, message: String, causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(id = id, message = message, cause = causedBy)

  def apply(id:       AnomalyID, parameters: Anomaly.Parameters, causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(id = id, params = parameters, cause = causedBy)

  def apply(message:  String, parameters:    Anomaly.Parameters, causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(message = message, params = parameters, cause = causedBy)

  def apply(
    id:               AnomalyID,
    message:          String,
    parameters:       Anomaly.Parameters,
    causedBy:         Throwable,
  ): UnhandledCatastrophe =
    UnhandledCatastropheImpl(id = id, message = message, params = parameters, cause = causedBy)

  def apply(a: AnomalyBase, causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(
      id      = a.id,
      message = a.message,
      params  = a.parameters,
      cause   = causedBy,
    )

  def apply(a: AnomalyBase): UnhandledCatastrophe =
    UnhandledCatastropheImpl(
      id      = a.id,
      message = a.message,
      params  = a.parameters,
      cause   = Anomaly(a),
    )

  def apply(message: String, causedBy: Throwable): UnhandledCatastrophe =
    UnhandledCatastropheImpl(message = message, cause = causedBy)
}

final private[pureharm] case class UnhandledCatastropheImpl(
  override val id:      AnomalyID = UnhandledCatastropheID,
  override val message: String = UnhandledCatastrophe.UnhandledCatastropheMsg,
  params:               Anomaly.Parameters = Anomaly.Parameters.empty,
  cause:                Throwable,
) extends UnhandledCatastrophe(message, cause = cause, params) {
  override val parameters: Anomaly.Parameters = super.parameters ++ params
}
