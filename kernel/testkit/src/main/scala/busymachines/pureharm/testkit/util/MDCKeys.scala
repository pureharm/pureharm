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
package busymachines.pureharm.testkit.util

import scala.concurrent.duration.FiniteDuration

import org.scalactic.source
import org.scalatest._

/** Common keys used in MDC contexts for loggers... allows us to be somewhat consistent
  * through the entire app especially since in logs these get sorted, so we can prefix
  * them universally
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Jun 2020
  */
object MDCKeys {
  protected val OutcomeK: String = "outcome"
  protected val TestName: String = "testName"
  protected val Duration: String = "duration"
  protected val Line:     String = "line"

  def lineNumber(i: Int):            (String, String) = Line     -> i.toString
  def testName(s:   String):         (String, String) = TestName -> s"'$s'"
  def duration(s:   FiniteDuration): (String, String) = Duration -> s.toString
  def outcome(o:    Outcome):        (String, String) = OutcomeK -> o.productPrefix

  def outcomeFailed:  (String, String) = OutcomeK -> Failed.toString
  def outcomeSuccess: (String, String) = OutcomeK -> Succeeded.productPrefix
  def outcomePending: (String, String) = OutcomeK -> Pending.productPrefix

  def apply(test: TestData): Map[String, String] =
    test.pos match {
      case None      =>
        Map(MDCKeys.testName(test.name))
      case Some(pos) =>
        this.apply(test.name, pos)
    }

  def apply(testName: String, position: source.Position): Map[String, String] =
    Map(MDCKeys.testName(testName), MDCKeys.lineNumber(position.lineNumber))

  def apply(out: Outcome, duration: FiniteDuration): Map[String, String] =
    Map(MDCKeys.outcome(out), MDCKeys.duration(duration))
}
