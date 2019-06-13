/**
  * Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.json.test.derivetest

import org.scalatest.flatspec.AnyFlatSpec
import busymachines.pureharm.json.implicits._
import busymachines.pureharm.json.test._

/**
  * Here we test [[busymachines.pureharm.json.Decoder]] derivation
  *
  * See the [[Melon]] hierarchy
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
final class JsonDefaultSemiAutoDecoderDerivationTest extends AnyFlatSpec {
  import melonsDefaultSemiAutoDecoders._

  //-----------------------------------------------------------------------------------------------

  it should "... be able to deserialize anarchist melon (i.e. not part of any hierarchy)" in {
    val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
    val rawJson =
      """
        |{
        |  "noGods" : true,
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin.trim

    val read = rawJson.unsafeDecodeAs[AnarchistMelon]
    assertResult(anarchistMelon)(read)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... fail to compile when there is no explicitly defined decoder for a type down in the hierarchy" in {
    assertDoesNotCompile(
      """
        |val rawJson = "{}"
        |rawJson.unsafeDecodeAs[WinterMelon]
      """.stripMargin,
    )
  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to deserialize case class from hierarchy when it is referred to as its super-type" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val rawJson =
      """
        |{
        |  "fuzzy" : true,
        |  "weight" : 45,
        |  "_type" : "WinterMelon"
        |}
      """.stripMargin.trim

    val read = rawJson.unsafeDecodeAs[Melon]
    assertResult(winterMelon)(read)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to deserialize case objects of the hierarchy" in {
    val smallMelon: Melon = SmallMelon
    val rawJson =
      """
        |{
        |  "_type" : "SmallMelon"
        |}
      """.stripMargin.trim
    val read = rawJson.unsafeDecodeAs[Melon]
    assertResult(smallMelon)(read)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... deserialize hierarchies of case objects as enums (i.e. plain strings)" in {
    val taste: List[Taste] = List(SweetTaste, SourTaste)
    val rawJson =
      """
        |[
        |  "SweetTaste",
        |  "SourTaste"
        |]
      """.stripMargin.trim

    val read = rawJson.unsafeDecodeAs[List[Taste]]
    assertResult(read)(taste)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... deserialize list of all case classes from the hierarchy" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val waterMelon:  Melon = WaterMelon(seeds = true, weight = 90)
    val smallMelon:  Melon = SmallMelon
    val squareMelon: Melon = SquareMelon(weight = 10, tastes = Seq(SourTaste, SweetTaste))
    val melons = List[Melon](winterMelon, waterMelon, smallMelon, squareMelon)

    val rawJson =
      """
        |
        |[
        |  {
        |    "fuzzy" : true,
        |    "weight" : 45,
        |    "_type" : "WinterMelon"
        |  },
        |  {
        |    "seeds" : true,
        |    "weight" : 90,
        |    "_type" : "WaterMelon"
        |  },
        |  {
        |    "_type" : "SmallMelon"
        |  },
        |  {
        |    "weight" : 10,
        |    "tastes" : [
        |      "SourTaste",
        |      "SweetTaste"
        |    ],
        |    "_type" : "SquareMelon"
        |  }
        |]
        |
        """.stripMargin.trim

    val read: List[Melon] = rawJson.unsafeDecodeAs[List[Melon]]
    assertResult(melons)(read)
  }

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------
}
