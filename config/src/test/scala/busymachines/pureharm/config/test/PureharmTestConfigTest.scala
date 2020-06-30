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
package busymachines.pureharm.config.test

import busymachines.pureharm.config._

import scala.concurrent.duration._
import busymachines.pureharm.effects._

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
final class PureharmTestConfigTest extends AnyFunSuite with Matchers {

  test("load config with phantom common types") {
    val read = PureharmTestConfig.default[IO]
    assert(
      read.unsafeRunSync() === PureharmTestConfig(
        PhantomInt(42),
        PhantomString("phantom"),
        PhantomBoolean(false),
        PhantomList(List(1, 2, 3)),
        PhantomSet(Set("value1", "value2")),
        PhantomFiniteDuration(10.minutes),
        PhantomDuration(10.minutes),
      )
    )
  }

  test("fail when loading invalid config") {
    val read = PureharmTestConfig.fromNamespace[IO]("pureharm.config.test.invalid")
    the[ConfigAggregateAnomalies] thrownBy read.unsafeRunSync()
  }
}
