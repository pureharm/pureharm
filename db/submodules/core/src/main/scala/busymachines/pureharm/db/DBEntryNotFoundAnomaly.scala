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

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
abstract class DBEntryNotFoundAnomaly(val pk: String, override val causedBy: Option[Throwable])
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
