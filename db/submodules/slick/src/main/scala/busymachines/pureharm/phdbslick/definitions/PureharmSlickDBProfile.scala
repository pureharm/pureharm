package busymachines.pureharm.phdbslick.definitions

import busymachines.pureharm.phdbslick.SlickQueryAlgebraDefinitions
import busymachines.pureharm.phdbslick.impl.{PureharmSlickInstances, SlickConnectionIOCatsInstances}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
trait PureharmSlickDBProfile extends PureharmDBSlickTypeDefinitions {
  self: slick.jdbc.JdbcProfile =>

  /**
    * We use this trick to propagate the profile from the top level object to the
    * definition of the "api" object. Otherwise we can't possibly reuse stuff that
    * should make a living there.
    *
    * Thus, in your app you should probably have something like the following:
    *
    * {{{
    *  //you need to depend on a specific support. For Postgres:
    *  //"com.github.tminglei" %% "slick-pg" % $slickPgV
    *  //https://github.com/tminglei/slick-pg
    *  import com.github.tminglei.slickpg._
    *
    * trait MyAppSlickProfile
    *   extends ExPostgresProfile
    *   with PureharmDBProfileMixin
    *   /* and all those other imports */ { self =>
    *
    * trait MyAppAPI extends super.API with PureharmAPIImplicits
    *
    *
    * object MyAppSlickProfile extends MyAppSlickProfile
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
    *
    * //RECOMMENDED — but not necessary
    *
    * {{{
    *   package myapp
    *
    *   package object myapp extends PureharmDBCoreTypeDefinitions with PureharmDBSlickTypeDefinitions {
    *     val implicits: MyAppSlickProfile.MyAppAPI = MyAppSlickProfile.api
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
    *
    */
  trait PureharmSlickAPIWithImplicits
      extends self.API with PureharmSlickInstances.PhantomTypeInstances with SlickConnectionIOCatsInstances
      with SlickQueryAlgebraDefinitions {
    final override protected val enclosingProfile: slick.jdbc.JdbcProfile = self

    final type ProvenShape[U] = slick.lifted.ProvenShape[U]
    final val ProvenShape: slick.lifted.ProvenShape.type = slick.lifted.ProvenShape
  }

  final def slickJDBCProfileAPI: SlickJDBCProfileAPI = this.api

  final def jdbcProfileAPI: JDBCProfileAPI = JDBCProfileAPI(slickJDBCProfileAPI)

}
