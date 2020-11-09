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
  * @since 10 Jun 2019
  */
abstract class InconsistentStateCatastrophe(
  override val message:  String,
  override val causedBy: Option[Throwable] = None,
) extends Catastrophe(message, causedBy) with Product with Serializable {
  override def id: AnomalyID = InconsistentStateCatastropheID
}

private[pureharm] case object InconsistentStateCatastropheID extends AnomalyID with Product with Serializable {
  override val name: String = "IS_0"
}

object InconsistentStateCatastrophe extends CatastropheConstructors[InconsistentStateCatastrophe] {
  private[pureharm] val InconsistentStateCatastropheMsg: String = "Inconsistent State Catastrophe"

  override def apply(causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = causedBy.getMessage, causedBy = Option(causedBy))

  override def apply(id:       AnomalyID, message: String, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, causedBy = Option(causedBy))

  override def apply(id:       AnomalyID, parameters: Anomaly.Parameters, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, params = parameters, causedBy = Option(causedBy))

  override def apply(
    message:                   String,
    parameters:                Anomaly.Parameters,
    causedBy:                  Throwable,
  ): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(
    id: AnomalyID,
    message:    String,
    parameters: Anomaly.Parameters,
    causedBy:   Throwable,
  ): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(a: AnomalyBase, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(
      id       = a.id,
      message  = a.message,
      params   = a.parameters,
      causedBy = Option(causedBy),
    )

  override def apply(id:         AnomalyID):            InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id)

  override def apply(message:    String):               InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message)

  override def apply(parameters: Anomaly.Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(params = parameters)

  override def apply(id:         AnomalyID, message: String): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message)

  override def apply(id:         AnomalyID, parameters: Anomaly.Parameters):          InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, params = parameters)

  override def apply(message:    String, parameters:    Anomaly.Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, params = parameters)

  override def apply(id:         AnomalyID, message: String, parameters: Anomaly.Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, params = parameters)

  override def apply(a:          AnomalyBase): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(
      id       = a.id,
      message  = a.message,
      params   = a.parameters,
      causedBy = Option(Anomaly(a)),
    )

  override def apply(message: String, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, causedBy = Option(causedBy))
}

final private[pureharm] case class InconsistentStateCatastropheImpl(
  override val id:       AnomalyID = InconsistentStateCatastropheID,
  override val message:  String = InconsistentStateCatastrophe.InconsistentStateCatastropheMsg,
  params:                Anomaly.Parameters = Anomaly.Parameters.empty,
  override val causedBy: Option[Throwable] = None,
) extends InconsistentStateCatastrophe(message, causedBy = causedBy) {
  override val parameters: Anomaly.Parameters = super.parameters ++ params
}
