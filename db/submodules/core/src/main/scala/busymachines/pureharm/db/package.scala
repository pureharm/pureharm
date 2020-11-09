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
package busymachines.pureharm

import busymachines.pureharm.phantom.PhantomType

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
package object db {
  final object DBHost extends PhantomType[String]

  /** Please include port in host, if needed, e.g.
    * {{{localhost:5432}}}
    */
  final type DBHost = DBHost.Type

  final object JDBCUrl extends PhantomType[String] {

    def postgresql(host: DBHost, db: DatabaseName): this.Type =
      this.apply(s"jdbc:postgresql://$host/$db")

    def postgresql(host: DBHost, db: DatabaseName, schema: SchemaName): this.Type =
      this.apply(s"jdbc:postgresql://$host/$db?currentSchema=$schema")
  }

  final type JDBCUrl = JDBCUrl.Type

  final object DBUsername extends PhantomType[String]
  final type DBUsername = DBUsername.Type

  final object DBPassword extends PhantomType[String]
  final type DBPassword = DBPassword.Type

  final object TableName extends PhantomType[String]
  final type TableName = TableName.Type

  final object DatabaseName extends PhantomType[String]
  final type DatabaseName = DatabaseName.Type

  final object SchemaName extends PhantomType[String] {
    def public: SchemaName = SchemaName("public")
  }
  final type SchemaName = SchemaName.Type

  @scala.deprecated("Use Repo instead, will be removed in 0.0.6-M3", "0.0.6-M2")
  final type DAOAlgebra[R[_], E, PK] = Repo[R, E, PK]
}
