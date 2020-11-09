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
abstract class NotImplementedCatastrophe(
  override val message: String,
  causedBy:             Option[Throwable] = None,
) extends Catastrophe(message, causedBy) with Product with Serializable {
  override def id: AnomalyID = NotImplementedCatastropheID
}

private[pureharm] case object NotImplementedCatastropheID extends AnomalyID with Product with Serializable {
  override val name: String = "NI_0"
}

object NotImplementedCatastrophe extends CatastropheConstructors[NotImplementedCatastrophe] {
  private[pureharm] val NotImplementedCatastropheMsg: String = "Not Implemented Catastrophe"

  override def apply(causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(message = causedBy.getMessage, causedBy = Option(causedBy))

  override def apply(id:       AnomalyID, message: String, causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, message = message, causedBy = Option(causedBy))

  override def apply(id:       AnomalyID, parameters: Anomaly.Parameters, causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, params = parameters, causedBy = Option(causedBy))

  override def apply(message:  String, parameters:    Anomaly.Parameters, causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(
    id:                        AnomalyID,
    message:                   String,
    parameters:                Anomaly.Parameters,
    causedBy:                  Throwable,
  ): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(a: AnomalyBase, causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(
      id       = a.id,
      message  = a.message,
      params   = a.parameters,
      causedBy = Option(causedBy),
    )

  override def apply(id:         AnomalyID):            NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id)

  override def apply(message:    String):               NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(message = message)

  override def apply(parameters: Anomaly.Parameters): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(params = parameters)

  override def apply(id:         AnomalyID, message: String): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, message = message)

  override def apply(id:         AnomalyID, parameters: Anomaly.Parameters):       NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, params = parameters)

  override def apply(message:    String, parameters:    Anomaly.Parameters): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(message = message, params = parameters)

  override def apply(id:         AnomalyID, message: String, parameters: Anomaly.Parameters): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(id = id, message = message, params = parameters)

  override def apply(a:          AnomalyBase): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(
      id       = a.id,
      message  = a.message,
      params   = a.parameters,
      causedBy = Option(Anomaly(a)),
    )

  override def apply(message: String, causedBy: Throwable): NotImplementedCatastrophe =
    NotImplementedCatastropheImpl(message = message, causedBy = Option(causedBy))
}

final private[pureharm] case class NotImplementedCatastropheImpl(
  override val id:       AnomalyID = NotImplementedCatastropheID,
  override val message:  String = NotImplementedCatastrophe.NotImplementedCatastropheMsg,
  params:                Anomaly.Parameters = Anomaly.Parameters.empty,
  override val causedBy: Option[Throwable] = None,
) extends NotImplementedCatastrophe(message, causedBy = causedBy) {
  override val parameters: Anomaly.Parameters = super.parameters ++ params
}
