/** Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.config.test

import busymachines.pureharm.effects._
import busymachines.pureharm.internals.config.ConfigSourceLoadingAnomaly
import busymachines.pureharm.testkit.PureharmTest

import scala.concurrent.duration._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
final class PureharmTestConfigFromDefaultSourceTest extends PureharmTest {

  test("load config from correct custom path") {
    val correct = new PureharmTestConfigLoaderFromSource("not-a-conf.txt")
    for {
      read <- correct.fromNamespace[IO]("pureharm.config.test")
    } yield assert(
      read === PureharmTestConfig(
        PhantomInt(81234),
        PhantomString("phantom-not-a-config"),
        PhantomBoolean(true),
        PhantomList(List(1, 2, 3)),
        PhantomSet(Set("value1", "value2")),
        PhantomFiniteDuration(10.minutes),
        PhantomDuration(10.minutes),
        SafePhantomInt.unsafe(123),
      )
    )
  }

  test("load config from incorrect custom path") {
    val incorrectSource = new PureharmTestConfigLoaderFromSource("please-no-NPE.txt")
    for {
      attempt <- incorrectSource.fromNamespace[IO]("pureharm.config.test").attempt
    } yield assertFailure[ConfigSourceLoadingAnomaly](attempt)
  }
}
