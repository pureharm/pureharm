package busymachines.pureharm.db

import busymachines.pureharm.anomaly._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
final case class DBEntryNotFoundAnomaly(pk: String, override val causedBy: Option[Throwable])
    extends NotFoundAnomaly(s"DB row with pk=$pk not found", causedBy) {
  override val id: AnomalyID = DBEntryNotFoundAnomaly.DBEntryNotFoundAnomalyID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    DBEntryNotFoundAnomaly.PK -> pk,
  )
}

object DBEntryNotFoundAnomaly {
  private val PK = "pk"
  case object DBEntryNotFoundAnomalyID extends AnomalyID { override val name: String = "ph_db_001" }
}
