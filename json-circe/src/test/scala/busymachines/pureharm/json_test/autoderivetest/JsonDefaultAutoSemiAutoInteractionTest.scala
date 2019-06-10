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
package busymachines.pureharm.json_test.autoderivetest

import org.scalatest.flatspec.AnyFlatSpec

import busymachines.pureharm.json._
import busymachines.pureharm.json.implicits._
import busymachines.pureharm.json_test._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
final class JsonDefaultAutoSemiAutoInteractionTest extends AnyFlatSpec {

  import autoderive._
  //moved outside of the scope of the test to avoid false positive "implicit not used" warning
  implicit val explicitImplicitTasteCodec: Codec[Taste] = derive.enumerationCodec[Taste]

  it should "... auto should use the explicit codec for sub-hierarchies of Melon" in {

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
      """.stripMargin.trim,
      "... raw json should have been obtained using the explicitImplicitTasteCodec",
    )(rawJson)

    val read: List[Melon] = rawJson.unsafeDecodeAs[List[Melon]]
    assertResult(melons)(read)
  }

}
