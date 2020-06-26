package busymachines.pureharm.db.testkit

import busymachines.pureharm.db.DAOAlgebra
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.testkit._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
abstract class PureharmDAOTest[E, PK, Trans] extends FixturePureharmTest {

  def data: PureharmDAOTestData[E, PK]

  def setup: PureharmDAOTestSetup[Trans]

  implicit val runtime: PureharmTestRuntime
  import runtime._

  def fixture(meta: MetaData, trans: Trans): Resource[IO, FixtureParam]

  override def fixture(meta: MetaData): Resource[IO, FixtureParam] =
    for {
      trans <- setup.dbTransactor(meta)
      fix   <- fixture(meta, trans)
    } yield fix

  override type FixtureParam <: DAOAlgebra[IO, E, PK]

  test("find — none") { implicit dao: FixtureParam =>
    for {
      att <- dao.find(data.randomPK)
    } yield assert(att.isEmpty)
  }
}
