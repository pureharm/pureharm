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
package busymachines.pureharm

import scala.annotation.implicitNotFound

import busymachines.pureharm.effects._
import busymachines.pureharm.phantom.PhantomType
import org.typelevel.log4cats._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  */
package object testkit {

  @implicitNotFound(
    msg = """
      |TestLogger is implemented by default in either:
      |  - busymachines.pureharm.testkit.FixturePureharmTest
      |  - busymachines.pureharm.testkit.PureharmTest
      | 
      |Depending on which one you use. And there is little reason to want
      |it from somewhere else. But you can always override it to plug
      |in a custom one.
      |
      |The purpose of TestLogger is to log everything related to test-setup/
      |tear-down to enrich whatever scalatest tells you
      |
      |"""
  )
  type TestLogger = TestLogger.Type

  object TestLogger extends PhantomType[StructuredLogger[IO]] {

    def fromClass(cl: Class[_]): TestLogger =
      this.apply(slf4j.Slf4jLogger.getLoggerFromName[IO](s"${cl.getCanonicalName}.test.report"))
  }

}
