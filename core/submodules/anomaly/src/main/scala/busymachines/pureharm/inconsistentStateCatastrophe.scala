package busymachines.pureharm

import busymachines.pureharm.Anomaly.Parameters

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  *
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

  override def apply(id: AnomalyID, message: String, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, causedBy = Option(causedBy))

  override def apply(id: AnomalyID, parameters: Parameters, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, parameters = parameters, causedBy = Option(causedBy))

  override def apply(message: String, parameters: Parameters, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, parameters = parameters, causedBy = Option(causedBy))

  override def apply(
    id:         AnomalyID,
    message:    String,
    parameters: Parameters,
    causedBy:   Throwable,
  ): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, parameters = parameters, causedBy = Option(causedBy))

  override def apply(a: Anomaly, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(
      id         = a.id,
      message    = a.message,
      parameters = a.parameters,
      causedBy   = Option(causedBy),
    )

  override def apply(id: AnomalyID): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id)

  override def apply(message: String): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message)

  override def apply(parameters: Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(parameters = parameters)

  override def apply(id: AnomalyID, message: String): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message)

  override def apply(id: AnomalyID, parameters: Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, parameters = parameters)

  override def apply(message: String, parameters: Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, parameters = parameters)

  override def apply(id: AnomalyID, message: String, parameters: Parameters): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(id = id, message = message, parameters = parameters)

  override def apply(a: Anomaly): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(
      id         = a.id,
      message    = a.message,
      parameters = a.parameters,
      causedBy   = Option(a),
    )

  override def apply(message: String, causedBy: Throwable): InconsistentStateCatastrophe =
    InconsistentStateCatastropheImpl(message = message, causedBy = Option(causedBy))
}

final private[pureharm] case class InconsistentStateCatastropheImpl(
  override val id:         AnomalyID          = InconsistentStateCatastropheID,
  override val message:    String             = InconsistentStateCatastrophe.InconsistentStateCatastropheMsg,
  override val parameters: Anomaly.Parameters = Anomaly.Parameters.empty,
  override val causedBy:   Option[Throwable]  = None,
) extends InconsistentStateCatastrophe(message, causedBy = causedBy)
