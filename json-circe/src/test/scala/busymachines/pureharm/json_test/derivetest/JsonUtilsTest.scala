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
package busymachines.pureharm.json_test.derivetest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{EitherValues, Matchers}
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.json._
import busymachines.pureharm.json_impl.{JsonDecoding, JsonParsing}
import busymachines.pureharm.json_test._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
final class JsonUtilsTest extends AnyFlatSpec with EitherValues with Matchers {

  import melonsDefaultSemiAutoCodecs._

  behavior of "JsonParsing.safe"

  //-----------------------------------------------------------------------------------------------

  it should ".... parse correct json" in {
    val rawJson =
      """
        |{
        |  "noGods" : true,
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    JsonParsing.parseString(rawJson).unsafeGet()
  }

  //-----------------------------------------------------------------------------------------------

  it should ".... fail on incorrect json" in {
    val rawJson =
      """
        |{
        |  "noGods" : true
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    an[JsonParsingAnomaly] shouldBe thrownBy {
      JsonParsing.parseString(rawJson).unsafeGet()
    }
  }

  //-----------------------------------------------------------------------------------------------

  behavior of "JsonParsing.unsafe"

  //-----------------------------------------------------------------------------------------------

  it should ".... parse correct json" in {
    val rawJson =
      """
        |{
        |  "noGods" : true,
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    JsonParsing.unsafeParseString(rawJson)
  }

  //-----------------------------------------------------------------------------------------------

  it should ".... throw exception on incorrect json" in {
    val rawJson =
      """
        |{
        |  "noGods" : true
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    an[JsonParsingAnomaly] shouldBe thrownBy {
      JsonParsing.unsafeParseString(rawJson)
    }
  }

  //-----------------------------------------------------------------------------------------------

  behavior of "JsonDecoding.safe"

  //-----------------------------------------------------------------------------------------------

  it should "... correctly decode when JSON, and representation are correct" in {
    val rawJson =
      """
        |{
        |  "noGods" : true,
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    val am = JsonDecoding.decodeAs[AnarchistMelon](rawJson).unsafeGet()
    assertResult(AnarchistMelon(noGods = true, noMasters = true, noSuperTypes = true))(am)
  }

  //-----------------------------------------------------------------------------------------------

  it should "... fail with parsing error when JSON has syntax errors" in {
    val rawJson =
      """
        |{
        |  "noGods" : true
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    an[JsonParsingAnomaly] shouldBe thrownBy {
      JsonDecoding.decodeAs[AnarchistMelon](rawJson).unsafeGet()
    }
  }

  //-----------------------------------------------------------------------------------------------

  it should "... fail with decoding error when JSON is syntactically correct, but encoding is wrong" in {
    val rawJson =
      """
        |{
        |  "noMasters" : true,
        |  "noSuperTypes" : true
        |}
      """.stripMargin

    the[JsonDecodingAnomaly] thrownBy {
      JsonDecoding.decodeAs[AnarchistMelon](rawJson).unsafeGet()
    }
  }

  //-----------------------------------------------------------------------------------------------

}
