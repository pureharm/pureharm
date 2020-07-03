package busymachines.pureharm.db.testkit

import busymachines.pureharm.effects._
import busymachines.pureharm.testkit.PureharmTestWithResource

abstract class DBTest[Trans] extends PureharmTestWithResource {
  def setup: DBTestSetup[Trans]

  def resource(meta: MetaData, trans: Trans): Resource[IO, ResourceType]

  override def resource(meta: MetaData): Resource[IO, ResourceType] =
    for {
      trans <- setup.transactor(meta)
      fix   <- resource(meta, trans)
    } yield fix
}
