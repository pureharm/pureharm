/**
  * Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.db

import busymachines.pureharm.anomaly._

//=============================================================================

final case class DBEntryNotFoundAnomaly(val pk: String, override val causedBy: Option[Throwable])
  extends NotFoundAnomaly(s"DB row with pk=$pk not found", causedBy) {
  override val id: AnomalyID = DBEntryNotFoundAnomaly.DBEntryNotFoundAnomalyID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.PK -> pk
  )
}

object DBEntryNotFoundAnomaly {
  case object DBEntryNotFoundAnomalyID extends AnomalyID { override val name: String = "ph_db_001" }
}

//=============================================================================

final case class DBBatchInsertFailedAnomaly(
  val expectedSize:      Int,
  val actualSize:        Int,
  override val causedBy: Option[Throwable],
) extends InvalidInputAnomaly(s"DB batch insert expected to insert $expectedSize but inserted $actualSize.", causedBy) {
  override val id: AnomalyID = DBBatchInsertFailedAnomaly.DBBatchUpdateFailedAnomalyID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.Expected -> expectedSize.toString,
    CommonKeys.Actual   -> actualSize.toString,
  )
}

object DBBatchInsertFailedAnomaly {
  case object DBBatchUpdateFailedAnomalyID extends AnomalyID { override val name: String = "ph_db_002" }
}

//=============================================================================

final case class DBDeleteByPKFailedAnomaly(
  val pk: String
) extends InvalidInputAnomaly(s"DELETE by PK=$pk did not delete anything ", None) {
  override val id: AnomalyID = DBDeleteByPKFailedAnomaly.DBDeleteByPKFailedID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.PK -> pk
  )
}

object DBDeleteByPKFailedAnomaly {
  case object DBDeleteByPKFailedID extends AnomalyID { override val name: String = "ph_db_003" }
}

//=============================================================================

final case class DBUniqueConstraintViolationAnomaly(
  val column: String,
  val value:  String,
) extends ConflictAnomaly(s"Unique key constrain violation: key=$column, value: $value", None) {
  override val id: AnomalyID = DBUniqueConstraintViolationAnomaly.DBUniqueConstraintViolationID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.Key   -> column,
    CommonKeys.Value -> value,
  )
}

object DBUniqueConstraintViolationAnomaly {
  case object DBUniqueConstraintViolationID extends AnomalyID { override val name: String = "ph_db_004" }
}

//=============================================================================

private[db] object CommonKeys {
  val PK       = "pk"
  val Key      = "key"
  val Value    = "value"
  val Expected = "expected"
  val Actual   = "actual"
}
