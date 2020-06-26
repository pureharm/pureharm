package busymachines.pureharm.db.testkit

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db._
import busymachines.pureharm.db.test._
import busymachines.pureharm.identifiable.Identifiable
import busymachines.pureharm.testkit._

/**
  *
  * Common class to enforce a minimal contract for out of the box implementations
  * of [[DAOAlgebra]] for various backends. For now only for doobie, and slick,
  * soon, hopefully for skunk too :D
  *
  * In your own production code you probably create something similar that
  * inherits from [[PureharmDAOTest]] to get a bunch of free tests for your
  * DAOs <3 to ensure that at least it typechecks.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
private[pureharm]abstract class PureharmRowTest[Trans] extends PureharmDAOTest[PureharmRow, PhantomPK, Trans] {
  override type FixtureParam <: PureharmRowDAO[IO]

  override def data: PureharmDAOTestData[PureharmRow, PhantomPK] = PureharmRowTest.pureharmRows

  override def setup: PureharmDAOTestSetup[Trans]

  implicit override val runtime: PureharmTestRuntime = PureharmTestRuntime

  override def fixture(meta: MetaData, trans: Trans): Resource[IO, FixtureParam]
}

object PureharmRowTest {

  object pureharmRows extends PureharmDAOTestData[PureharmRow, PhantomPK] {
    override def iden: Identifiable[PureharmRow, PhantomPK] = PureharmRow.identifiable

    override def row1: PureharmRow = PureharmRow(
      id          = PhantomPK("row1_id"),
      byte        = PhantomByte(245.toByte),
      int         = PhantomInt(41),
      long        = PhantomLong(0.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string      = PhantomString("row1_string"),
      jsonbCol    = PureharmJSONCol(42, "row1_json_column"),
      optionalCol = Option(PhantomString("row1_optional_value")),
    )

    override val row2: PureharmRow = PureharmRow(
      id          = PhantomPK("row2_id"),
      byte        = PhantomByte(123.toByte),
      int         = PhantomInt(4321),
      long        = PhantomLong(12L),
      bigDecimal  = PhantomBigDecimal(BigDecimal("23414")),
      string      = PhantomString("row2_string"),
      jsonbCol    = PureharmJSONCol(44, "row2_json_column"),
      optionalCol = Option(PhantomString("row2_optional_value")),
    )

    override def randomPK: PhantomPK = PhantomPK("120-3921-039213")
  }
}
