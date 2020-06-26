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
package busymachines.pureharm.db.testdata

import busymachines.pureharm.effects._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.identifiable._

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

  override def fixture(meta: MetaData, trans: Trans): Resource[IO, FixtureParam]
}

object PHTRowDAOTest {

  object pureharmRows extends PureharmDAOTestData[PHTRow, PhantomPK] {
    override def iden: Identifiable[PHTRow, PhantomPK] = PHTRow.identifiable

    override def row1: PHTRow = PHTRow(
      id          = PhantomPK("row1_id"),
      byte        = PhantomByte(245.toByte),
      int         = PhantomInt(41),
      long        = PhantomLong(0.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string      = PhantomString("row1_string"),
      jsonbCol    = PHTJSONCol(42, "row1_json_column"),
      optionalCol = Option(PhantomString("row1_optional_value")),
    )

    override val row2: PHTRow = PHTRow(
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
