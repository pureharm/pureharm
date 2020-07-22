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

import busymachines.pureharm.db.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.identifiable._

/**
  * Common class to enforce a minimal contract for out of the box implementations
  * of [[busymachines.pureharm.db.Repo]] for various backends. For now only for doobie, and slick,
  * soon, hopefully for skunk too :D
  *
  * In your own production code you probably create something similar that
  * inherits from [[RepoTest]] to get a bunch of free tests for your
  * DAOs <3 to ensure that at least it typechecks.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  */
abstract private[pureharm] class PHRowRepoTest[Trans] extends RepoTest[PHRow, PhantomPK, Trans] {
  override type ResourceType <: PHRowRepo[IO]

  override def data: PHRowRepoTest.pureharmRows.type = PHRowRepoTest.pureharmRows
}

object PHRowRepoTest {

  object pureharmRows extends RepoTestData[PHRow, PhantomPK] {
    override def iden: Identifiable[PHRow, PhantomPK] = PHRow.identifiable

    override def row1: PHRow = PHRow(
      id           = PhantomPK("row1_id"),
      byte         = PhantomByte(245.toByte),
      int          = PhantomInt(41),
      long         = PhantomLong(0.toLong),
      bigDecimal   = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string       = PhantomString("row1_string"),
      jsonbCol     = PHJSONCol(42, "row1_json_column"),
      optionalCol  = Option(PhantomString("row1_optional_value")),
      uniqueString = UniqueString("unique_string_value_1"),
      uniqueInt    = UniqueInt(490),
      uniqueJSON   = UniqueJSON(PHJSONCol(421, "unique_json_value_1")),
    )

    override val row2: PHRow = PHRow(
      id           = PhantomPK("row2_id"),
      byte         = PhantomByte(123.toByte),
      int          = PhantomInt(4321),
      long         = PhantomLong(12L),
      bigDecimal   = PhantomBigDecimal(BigDecimal("23414")),
      string       = PhantomString("row2_string"),
      jsonbCol     = PHJSONCol(44, "row2_json_column"),
      optionalCol  = Option(PhantomString("row2_optional_value")),
      uniqueString = UniqueString("unique_string_value_2"),
      uniqueInt    = UniqueInt(491),
      uniqueJSON   = UniqueJSON(PHJSONCol(421, "unique_json_value_2")),
    )

    override def nonExistentPK: PhantomPK = PhantomPK("120-3921-039213")

    override val row1Update1: PHRow = row1.copy(
      byte         = PhantomByte(111.toByte),
      int          = PhantomInt(42),
      long         = PhantomLong(6.toLong),
      bigDecimal   = PhantomBigDecimal(BigDecimal("328572")),
      string       = PhantomString("updated_string"),
      jsonbCol     = PHJSONCol(79, "new_json_col"),
      optionalCol  = Option(PhantomString("new opt_value")),
      uniqueString = UniqueString("unique_string_value_1_update"),
      uniqueInt    = UniqueInt(49012),
      uniqueJSON   = UniqueJSON(PHJSONCol(4121, "unique_json_value_1_update")),
    )

    override val row1Update2: PHRow = row1.copy(
      byte        = PhantomByte(5.toByte),
      int         = PhantomInt(31),
      long        = PhantomLong(988888.toLong),
      bigDecimal  = PhantomBigDecimal(BigDecimal("89276")),
      string      = PhantomString("updated_string_2"),
      jsonbCol    = PHJSONCol(1, "new_json_col_2"),
      optionalCol = Option.empty,
    )

    val ext1: ExtPHRow = ExtPHRow(
      id    = PhantomUUID.unsafeFromString("9320e4a5-a736-4623-96d4-92c61ce1c5cf"),
      rowID = row1.id,
    )

    val extNoFPK: ExtPHRow = ExtPHRow(
      id    = PhantomUUID.unsafeFromString("014ef766-232a-4952-87b8-f6b051dbf7d1"),
      rowID = nonExistentPK,
    )
  }

}
