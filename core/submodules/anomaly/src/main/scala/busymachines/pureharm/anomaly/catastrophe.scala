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

/** [[java.lang.Error]] is a reasonable choice for a super-class.
  * It's not caught by NonFatal(_)pattern matches. Which means that
  * we can properly propagate "irrecoverable errors" one a per "request"
  * basis and at the same time not crash our application into oblivion.
  */
abstract class Catastrophe(
  override val message: String,
  val causedBy:         Option[Throwable] = None,
) extends Error(message, causedBy.orNull) with CatastropheBase with Product with Serializable {
  override def id: AnomalyID = CatastropheID
}

trait CatastropheBase extends AnomalyBase with Product with Serializable

private[pureharm] case object CatastropheID extends AnomalyID with Product with Serializable {
  override val name: String = "CATA_0"
}

object Catastrophe extends CatastropheConstructors[Catastrophe] {
  private[pureharm] val CatastropheMsg = "Catastrophe"

  override def apply(causedBy:   Throwable): Catastrophe =
    CatastropheImpl(message = causedBy.getMessage, causedBy = Option(causedBy))

  override def apply(id:         AnomalyID, message: String, causedBy: Throwable): Catastrophe =
    CatastropheImpl(id = id, message = message, causedBy = Option(causedBy))

  override def apply(id:         AnomalyID, parameters: Anomaly.Parameters, causedBy: Throwable):  Catastrophe =
    CatastropheImpl(id = id, params = parameters, causedBy = Option(causedBy))

  override def apply(message:    String, parameters:    Anomaly.Parameters, causedBy: Throwable): Catastrophe =
    CatastropheImpl(message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(id:         AnomalyID, message:    String, parameters: Anomaly.Parameters, causedBy: Throwable): Catastrophe =
    CatastropheImpl(id = id, message = message, params = parameters, causedBy = Option(causedBy))

  override def apply(a:          AnomalyBase, causedBy: Throwable): Catastrophe =
    CatastropheImpl(id = a.id, message = a.message, params = a.parameters, causedBy = Option(causedBy))

  override def apply(id:         AnomalyID): Catastrophe =
    CatastropheImpl(id = id)

  override def apply(message:    String):               Catastrophe                  =
    CatastropheImpl(message = message)

  override def apply(parameters: Anomaly.Parameters): Catastrophe =
    CatastropheImpl(params = parameters)

  override def apply(id:         AnomalyID, message: String): Catastrophe =
    CatastropheImpl(id = id, message = message)

  override def apply(id:         AnomalyID, parameters: Anomaly.Parameters):          Catastrophe =
    CatastropheImpl(id = id, params = parameters)

  override def apply(message:    String, parameters:    Anomaly.Parameters): Catastrophe =
    CatastropheImpl(message = message, params = parameters)

  override def apply(id:         AnomalyID, message: String, parameters: Anomaly.Parameters): Catastrophe =
    CatastropheImpl(id = id, message = message, params = parameters)

  override def apply(a:          AnomalyBase): Catastrophe =
    InconsistentStateCatastropheImpl(
      id       = a.id,
      message  = a.message,
      params   = a.parameters,
      causedBy = Option(Anomaly(a)),
    )

  override def apply(message: String, causedBy: Throwable): Catastrophe =
    CatastropheImpl(message = message, causedBy = Option(causedBy))
}

final private[pureharm] case class CatastropheImpl(
  override val id:       AnomalyID = CatastropheID,
  override val message:  String = Catastrophe.CatastropheMsg,
  params:                Anomaly.Parameters = Anomaly.Parameters.empty,
  override val causedBy: Option[Throwable] = None,
) extends Catastrophe(message, causedBy = causedBy) {
  override val parameters: Anomaly.Parameters = super.parameters ++ params
}

//=============================================================================
//=============================================================================
//=============================================================================
