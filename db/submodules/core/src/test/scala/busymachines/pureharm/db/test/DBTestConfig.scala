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
package busymachines.pureharm.db.test

import busymachines.pureharm.db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 27 Jan 2020
  *
  */
object DBTestConfig {

  /**
    * All these values come from this file:
    * db/docker-pureharm-postgresql-test.sh
    *
    */
  val dbConfig: DBConnectionConfig = DBConnectionConfig(
    host     = DBHost("localhost:20010"),
    dbName   = DatabaseName("pureharm_test"),
    username = DBUsername("pureharmony"),
    password = DBPassword("pureharmony"),
    schema   = Option.empty, //Modify in each test accordingly before using
  )

  def schemaName(s: String): Option[SchemaName] = Option(SchemaName(s"pureharm_test_$s"))


}
