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
package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.implicits._
import busymachines.pureharm.identifiable.{FieldName, Identifiable}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
abstract class TableWithPK[E, PK](implicit val iden: Identifiable[E, PK]) {
  def tableName: TableName

  def pkFieldName: FieldName = iden.fieldName
  def tailColumns: List[ColumnName]

  def pkColumn: ColumnName = ColumnName(Fragment.const(iden.fieldName))
  def columns:  Row        = Row(NEList(pkColumn, tailColumns))

  /**
    * This yields an SQL fragment of the form:
    * column_name1, column_name2, column_name3, etc
    */
  def tuple: Fragment = Row.asFragment(columns)

  /**
    * Like [[tuple]] but with parentheses
    * (column_name1, column_name2, column_name3)
    */
  def tupleParens: Fragment = fr0"(" ++ tuple ++ fr0")"
}
