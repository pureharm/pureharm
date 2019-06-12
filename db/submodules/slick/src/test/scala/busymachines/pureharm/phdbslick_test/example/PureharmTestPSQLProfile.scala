package busymachines.pureharm.phdbslick_test.example

import busymachines.pureharm.dbslick.PureharmSlickDBProfile
import com.github.tminglei.slickpg.ExPostgresProfile

/**
  *
  * This is an example of the recommended way of setting up your Pureharm slickDB
  * profile object (sans a lot of other compatibility things
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
object PureharmTestPSQLProfile extends ExPostgresProfile with PureharmSlickDBProfile {

  trait PureharmTestAPI  extends super.API with PureharmSlickAPIWithImplicits
  object PureharmTestAPI extends PureharmTestAPI

  override val api: PureharmTestAPI = PureharmTestAPI
}
