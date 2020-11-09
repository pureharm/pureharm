/** Copyright (c) 2019 BusyMachines
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
  expectedSize:          Int,
  actualSize:            Int,
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
  pk: String
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
  column: String,
  value:  String,
) extends ConflictAnomaly(s"Unique key constraint violation: column=$column, value: $value", None) {
  override val id: AnomalyID = DBUniqueConstraintViolationAnomaly.DBUniqueConstraintViolationID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.Column -> column,
    CommonKeys.Value  -> value,
  )
}

object DBUniqueConstraintViolationAnomaly {
  case object DBUniqueConstraintViolationID extends AnomalyID { override val name: String = "ph_db_004" }
}

//=============================================================================

final case class DBForeignKeyConstraintViolationAnomaly(
  table:        String,
  constraint:   String,
  column:       String,
  value:        String,
  foreignTable: String,
) extends ConflictAnomaly(
    s"Foreign key constraint violation table=$table, constraint=$constraint. Column=$column, value=$value, foreign_table=$foreignTable",
    None,
  ) {
  override val id: AnomalyID = DBForeignKeyConstraintViolationAnomaly.DBForeignKeyConstraintViolationID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    CommonKeys.Table        -> table,
    CommonKeys.Constraint   -> constraint,
    CommonKeys.Column       -> column,
    CommonKeys.Value        -> value,
    CommonKeys.ForeignTable -> foreignTable,
  )
}

object DBForeignKeyConstraintViolationAnomaly {
  case object DBForeignKeyConstraintViolationID extends AnomalyID { override val name: String = "ph_db_005" }
}

//=============================================================================
private[db] object CommonKeys {
  val PK           = "pk"
  val Column       = "column"
  val Constraint   = "constraint"
  val Table        = "table"
  val ForeignTable = "table_foreign"
  val Value        = "value"
  val Expected     = "expected"
  val Actual       = "actual"
}
