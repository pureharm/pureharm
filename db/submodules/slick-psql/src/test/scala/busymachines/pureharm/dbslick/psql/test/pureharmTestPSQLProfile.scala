/**
  * Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.dbslick.psql.test

import busymachines.pureharm.db.PureharmDBCoreTypeDefinitions
import busymachines.pureharm.dbslick.psql.PureharmSlickPostgresProfile
import slick.jdbc.PostgresProfile

/**
  *
  * This is an example of the recommended way of setting up your Pureharm slickDB
  * profile object (sans a lot of other compatibility things)
  *
  * ---------
  *
  * !!!! N.B. !!!!
  *
  * You have to define the trait, and inherit from it in an object. It's
  * a known bug:
  * https://github.com/tminglei/slick-pg/issues/387
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
private[test] trait PureharmTestPSQLProfile extends PostgresProfile with PureharmSlickPostgresProfile {
  override val api: PureharmTestAPI = new PureharmTestAPI {}

  trait PureharmTestAPI extends super.API with PureharmSlickPostgresAPIWithImplicits {
    //if you need to use old "json" column type instead of the default "jsonb"
    //override protected def jsonPostgresType = busymachines.pureharm.dbslick.psql.PostgresqlJSON.json
  }
}

private[test] object testdb extends PureharmTestPSQLProfile with PureharmDBCoreTypeDefinitions {
  val implicits: PureharmTestAPI = this.api

}
