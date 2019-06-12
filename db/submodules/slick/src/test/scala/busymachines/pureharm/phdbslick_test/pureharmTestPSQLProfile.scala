package busymachines.pureharm.phdbslick_test

import busymachines.pureharm.dbslick.PureharmSlickDBProfile
import busymachines.pureharm.phdb.PureharmDBCoreTypeDefinitions
import com.github.tminglei.slickpg.ExPostgresProfile

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
private[phdbslick_test] trait PureharmTestPSQLProfile extends ExPostgresProfile with PureharmSlickDBProfile {
  override val api: PureharmTestAPI = new PureharmTestAPI {}

  trait PureharmTestAPI extends super.API with PureharmSlickAPIWithImplicits
}

private[phdbslick_test] object db extends PureharmTestPSQLProfile with PureharmDBCoreTypeDefinitions {

  val implicits: PureharmTestAPI = this.api

}
