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
import busymachines.pureharm.json.implicits._
import busymachines.pureharm.json_test._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
final class JsonAutoDerivationWithSpecialConfigurationTest1 extends AnyFlatSpec {

  /**
    * unfortunately this exclusion is absolutely necessary if you want to use the non-default _type
    * discriminator for sealed hierarchies of classes AND auto-derivation at the same time
    */
  import busymachines.pureharm.json.{defaultDerivationConfiguration => _, _}

  implicit val _melonManiaDiscriminatorConfig: Configuration =
    Configuration.default.withDiscriminator("_melonMania")

  /**
    * We explicitly create a codec for the enumeration of case objects, thus, it should not have _melonMania discriminator
    */
  implicit val explicitImplicitTasteCodec: Codec[Taste] = derive.enumerationCodec[Taste]

  it should "... type discriminator should be _melonMania, and case object hierarchy codec should be string only" in {

    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val waterMelon:  Melon = WaterMelon(seeds = true, weight = 90)
    val smallMelon:  Melon = SmallMelon
    val squareMelon: Melon = SquareMelon(weight = 10, tastes = Seq(SourTaste, SweetTaste))
    val melons = List[Melon](winterMelon, waterMelon, smallMelon, squareMelon)

    import autoderive._

    val rawJson = melons.asJson.spaces2

    assertResult(
      """
        |
        |[
        |  {
        |    "fuzzy" : true,
        |    "weight" : 45,
        |    "_melonMania" : "WinterMelon"
        |  },
        |  {
        |    "seeds" : true,
        |    "weight" : 90,
        |    "_melonMania" : "WaterMelon"
        |  },
        |  {
        |    "_melonMania" : "SmallMelon"
        |  },
        |  {
        |    "weight" : 10,
        |    "tastes" : [
        |      "SourTaste",
        |      "SweetTaste"
        |    ],
        |    "_melonMania" : "SquareMelon"
        |  }
        |]
      """.stripMargin.trim,
    )(rawJson)

    val read: List[Melon] = rawJson.unsafeDecodeAs[List[Melon]]
    assertResult(melons)(read)
  }

}

/**
  * We split the test classes in two, to move the implicit ``explicitImplicitTasteCodec``
  * from above to the scope of the class to remove false positive "implicit not used",
  * and it interferes with this test.
  */
final class JsonAutoDerivationWithSpecialConfigurationTest2 extends AnyFlatSpec {

  /**
    * unfortunately this exclusion is absolutely necessary if you want to use the non-default _type
    * discriminator for sealed hierarchies of classes AND auto-derivation at the same time
    */
  import busymachines.pureharm.json.{defaultDerivationConfiguration => _, _}

  implicit val _melonManiaDiscriminatorConfig: Configuration =
    Configuration.default.withDiscriminator("_melonMania")

  it should "... type discriminator should be _melonMania, and case object hierarchy serialization should also be affected by this new configuration" in {
    val winterMelon: Melon = WinterMelon(fuzzy = true, weight = 45)
    val waterMelon:  Melon = WaterMelon(seeds = true, weight = 90)
    val smallMelon:  Melon = SmallMelon
    val squareMelon: Melon = SquareMelon(weight = 10, tastes = Seq(SourTaste, SweetTaste))
    val melons = List[Melon](winterMelon, waterMelon, smallMelon, squareMelon)

    import autoderive._

    val rawJson = melons.asJson.spaces2
    assertResult(
      """
        |
        |[
        |  {
        |    "fuzzy" : true,
        |    "weight" : 45,
        |    "_melonMania" : "WinterMelon"
        |  },
        |  {
        |    "seeds" : true,
        |    "weight" : 90,
        |    "_melonMania" : "WaterMelon"
        |  },
        |  {
        |    "_melonMania" : "SmallMelon"
        |  },
        |  {
        |    "weight" : 10,
        |    "tastes" : [
        |      {
        |        "_melonMania" : "SourTaste"
        |      },
        |      {
        |        "_melonMania" : "SweetTaste"
        |      }
        |    ],
        |    "_melonMania" : "SquareMelon"
        |  }
        |]
      """.stripMargin.trim,
    )(rawJson)

    val read: List[Melon] = rawJson.unsafeDecodeAs[List[Melon]]
    assertResult(melons)(read)
  }

}
