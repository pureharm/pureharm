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
package busymachines.pureharm.db.psql

import cats.Show
import org.postgresql.util.PSQLException

object PSQLExceptionDebug {

  def apply(e: PSQLException): String = PSQLExceptionShow.show(e)

  implicit val PSQLExceptionShow: Show[PSQLException] = Show.show { e =>
    val msg = e.getServerErrorMessage
    s"""
       |
       |---------------------
       |class    = ${e.getClass.getCanonicalName}
       |toString = ${e.toString}
       |
       |msg parts:
       |
       |state      = ${e.getSQLState}
       |column     = ${msg.getColumn}
       |datatype   = ${msg.getDatatype}
       |detail     = ${msg.getDetail}
       |file       = ${msg.getFile}
       |hint       = ${msg.getHint}
       |line       = ${msg.getLine}
       |message    = ${msg.getMessage}
       |position   = ${msg.getPosition}
       |routine    = ${msg.getRoutine}
       |schema     = ${msg.getSchema}
       |severity   = ${msg.getSeverity}
       |table      = ${msg.getTable}
       |where      = ${msg.getWhere}
       |constraint = ${msg.getConstraint}
       |internal_query    = ${msg.getInternalQuery}
       |internal_position = ${msg.getInternalPosition}
       |""".stripMargin
  }
}
