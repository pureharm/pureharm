/**
  * Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.dbslick.psql

import busymachines.pureharm.db.PureharmDBCoreTypeDefinitions
import busymachines.pureharm.dbslick._
import busymachines.pureharm.internals.dbslick._
import busymachines.pureharm.internals.dbslick.psql

/**
  * If by any chance you are not using Postgres, then
  * you should be using [[PureharmSlickDBProfile]] as a
  * starting point and mixing in whatever you have specific
  * for your database.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
@scala.deprecated("Will be removed in the future, use the same type from the pureharm-db-slick module", "0.0.6-M3")
trait PureharmSlickPostgresProfile
  extends slick.jdbc.PostgresProfile with PureharmDBCoreTypeDefinitions with PureharmDBSlickTypeDefinitions { self =>

  /**
    * We use this trick to propagate the profile from the top level object to the
    * definition of the "api" object. Otherwise we can't possibly reuse stuff that
    * should make a living there.
    *
    * Thus, in your app you should probably have something like the following:
    *
    * {{{
    *
    * trait MyAppSlickProfile
    *   extends PureharmSlickPostgresProfile
    *   /* and all those other imports */ { self =>
    *
    *    override val api: MyAppSlickProfileAPI = new MyAppSlickProfileAPI {}
    *
    *    trait MyAppSlickProfileAPI extends super.API with PureharmSlickPostgresAPIWithImplicits
    * }
    * }}}
    *
    * And when you do the following imports:
    * {{{
    *   //this gives you all extra type definitions from pureharm
    *   import MyAppSlickProfile._
    *
    *   //this gives you all important instances provided by pureharm
    *   //like phantomType column types
    *   import MyAppSlickProfile.api._
    * }}}
    *
    * //RECOMMENDED — but not necessary
    *
    * {{{
    *   package myapp
    *
    *   object db extends MyAppSlickProfile {
    *     val implicits: MyAppSlickProfile.MyAppAPI = this.api
    *   }
    * }}}
    *
    * This creates more uniformity in the way you wind up doing imports in pureharm
    * since at calls sites you do:
    * {{{
    *   import myapp.effects._
    *   import myapp.effects.implicits._ //see how to use pureharm-effects-cats
    *   import myapp.db._
    *   import myapp.db.implicits._ //see how to use pureharm-effects-cats
    *
    *   class MyAppDomainSlickDAOIMPL {
    *     //dao method implementations
    *   }
    * }}}
    *
    * Basically, any imports of "pureharm-style" util packages bring in important types. Usually
    * enough to declare APIs and whatnot.
    *
    * While imports of the associated "implicits" brings you everything you need to
    * actually implement things.
    */
  @scala.deprecated("Will be removed in the future, use the same type from the pureharm-db-slick module", "0.0.6-M3")
  trait PureharmSlickPostgresAPIWithImplicits
    extends this.API with PureharmSlickInstances.PhantomTypeInstances with SlickConnectionIOCatsInstances
    with PureharmSlickConnectionIOOps.Implicits with SlickRepoQueriesDefinitions with SlickAliases
    with psql.SlickPostgresCirceSupportAPI {
    final override protected val enclosingProfile:         slick.jdbc.JdbcProfile     = self
    final override protected val enclosingPostgresProfile: slick.jdbc.PostgresProfile = self

    final type PostgresqlJSON = psql.PostgresqlJSON
    final val PostgresqlJSON: psql.PostgresqlJSON.type = psql.PostgresqlJSON
  }

  final def slickJDBCProfileAPI: SlickJDBCProfileAPI = this.api

  final def jdbcProfileAPI: JDBCProfileAPI = JDBCProfileAPI(slickJDBCProfileAPI)

}
