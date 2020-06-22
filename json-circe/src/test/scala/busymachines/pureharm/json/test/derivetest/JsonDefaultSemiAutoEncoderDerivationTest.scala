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
  *
  * Here we test [[busymachines.pureharm.json.Encoder]] derivation
  *
  * See the [[Melon]] hierarchy
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
final class JsonDefaultSemiAutoEncoderDerivationTest extends AnyFlatSpec {

  import melonsDefaultSemiAutoEncoders._

  //-----------------------------------------------------------------------------------------------

  it should "... be able to serialize anarchist melon (i.e. not part of any hierarchy)" in {
    val anarchistMelon = AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true)
    val rawJson        = anarchistMelon.asJson.spaces2

    assertResult(
      """
        |{
        |  "noGods" : true,
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin.trim
    )(rawJson)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... fail to compile when there is no defined encoder for a type down in the hierarchy" in {
    assertDoesNotCompile(
      """
        |val winterMelon: WinterMelon = WinterMelon(fuzzy = true, weight = 45)
        |winterMelon.asJson
      """.stripMargin
    )
  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to serialize case class from hierarchy when it is referred to as its super-type" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val rawJson = winterMelon.asJson.spaces2

    assertResult(
      """
        |{
        |  "fuzzy" : true,
        |  "weight" : 45,
        |  "_type" : "WinterMelon"
        |}
      """.stripMargin.trim
    )(rawJson)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... be able to serialize case objects of the hierarchy" in {
    val smallMelon: Melon = SmallMelon
    val rawJson = smallMelon.asJson.spaces2
    assertResult(
      """
        |{
        |  "_type" : "SmallMelon"
        |}
      """.stripMargin.trim
    )(rawJson)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... serialize hierarchies of case objects as enums (i.e. plain strings)" in {
    val taste: List[Taste] = List(SweetTaste, SourTaste)

    val rawJson = taste.asJson.spaces2
    assertResult(
      """
        |[
        |  "SweetTaste",
        |  "SourTaste"
        |]
      """.stripMargin.trim
    )(rawJson)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... serialize list of all case classes from the hierarchy" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val waterMelon:  Melon = WaterMelon(seeds = true, weight = 90)
    val smallMelon:  Melon = SmallMelon
    val squareMelon: Melon = SquareMelon(weight = 10, tastes = Seq(SourTaste, SweetTaste))
    val melons = List[Melon](winterMelon, waterMelon, smallMelon, squareMelon)

    val rawJson = melons.asJson.spaces2
    assertResult(
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
    )(rawJson)
  }
  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------

  //-----------------------------------------------------------------------------------------------
}
