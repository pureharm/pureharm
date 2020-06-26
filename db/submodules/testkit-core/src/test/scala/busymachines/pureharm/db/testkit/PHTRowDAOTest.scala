package busymachines.pureharm.db.testkit

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db.{testkit, _}
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
private[pureharm]abstract class PHTRowDAOTest[Trans] extends PureharmDAOTest[PHTRow, PhantomPK, Trans] {
  override type FixtureParam <: PHTDAO[IO]

  override def data: PureharmDAOTestData[PHTRow, PhantomPK] = PHTRowDAOTest.pureharmRows

  override def setup: PureharmDAOTestSetup[Trans]

  implicit override val runtime: PureharmTestRuntime = PureharmTestRuntime

  override def fixture(meta: MetaData, trans: Trans): Resource[IO, FixtureParam]
}

object PHTRowDAOTest {

  object pureharmRows extends PureharmDAOTestData[PHTRow, PhantomPK] {
    override def iden: Identifiable[PHTRow, PhantomPK] = PHTRow.identifiable

    override def row1: PHTRow = testkit.PHTRow(
      id          = PhantomPK("row1_id"),
      byte        = PhantomByte(245.toByte),
      int         = PhantomInt(41),
      long        = PhantomLong(0.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string      = PhantomString("row1_string"),
      jsonbCol    = PHTJSONCol(42, "row1_json_column"),
      optionalCol = Option(PhantomString("row1_optional_value")),
    )

    override val row2: PHTRow = testkit.PHTRow(
      id          = PhantomPK("row2_id"),
      byte        = PhantomByte(123.toByte),
      int         = PhantomInt(4321),
      long        = PhantomLong(12L),
      bigDecimal  = PhantomBigDecimal(BigDecimal("23414")),
      string      = PhantomString("row2_string"),
      jsonbCol    = PHTJSONCol(44, "row2_json_column"),
      optionalCol = Option(PhantomString("row2_optional_value")),
    )

    override def nonExistentPK: PhantomPK = PhantomPK("120-3921-039213")

    override val row1Update1: PHTRow = row1.copy(
      byte        = PhantomByte(111.toByte),
      int         = PhantomInt(42),
      long        = PhantomLong(6.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("328572")),
      string      = PhantomString("updated_string"),
      jsonbCol    = PHTJSONCol(79, "new_json_col"),
      optionalCol = Option(PhantomString("new opt_value")),
    )

    override val row1Update2: PHTRow = row1.copy(
      byte        = PhantomByte(5.toByte),
      int         = PhantomInt(31),
      long        = PhantomLong(988888.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("89276")),
      string      = PhantomString("updated_string_2"),
      jsonbCol    = PHTJSONCol(1, "new_json_col_2"),
      optionalCol = Option.empty,
    )
  }


}
