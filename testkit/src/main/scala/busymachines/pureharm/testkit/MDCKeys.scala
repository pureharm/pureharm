package busymachines.pureharm.testkit

import org.scalactic.source
import org.scalatest._

import scala.concurrent.duration.FiniteDuration

/**
  *
  * Common keys used in MDC contexts for loggers... allows us to be somewhat consistent
  * through the entire app especially since in logs these get sorted, so we can prefix
  * them universally
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Jun 2020
  *
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
