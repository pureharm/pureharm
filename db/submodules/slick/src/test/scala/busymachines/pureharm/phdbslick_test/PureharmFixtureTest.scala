package busymachines.pureharm.phdbslick_test

import org.scalatest._
import org.scalatest.funsuite.FixtureAnyFunSuite
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import db._

import org.scalactic.source
/**
  *
  * This is an experimental base class,
  * at some point it should be moved to a testkit module
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
abstract class PureharmFixtureTest extends FixtureAnyFunSuite {

  //for tests if fine if we just dump everything in global EC.
  //But for production this is an absolute nightmare and you should never do this
  implicit lazy val connectionIOEC: ConnectionIOEC   = ConnectionIOEC(ExecutionContext.global)
  implicit lazy val contextShift:   ContextShift[IO] = IO.contextShift(connectionIOEC)
  implicit lazy val timer:          Timer[IO]        = IO.timer(connectionIOEC)

  import io.chrisdavenport.log4cats._

  private val logger = slf4j.Slf4jLogger.getLoggerFromName[IO]("PureharmFixtureTest.reporter")

  /**
    * Instead of the "before and after shit" simply init, and close
    * everything in this Resource...
    */
  def fixture: Resource[IO, FixtureParam]

  protected def iotest(
    testName: String,
    testTags: Tag*,
  )(
    fun:          FixtureParam => IO[_],
  )(implicit pos: source.Position): Unit =
    test(testName, testTags: _*)(fp => fun(fp).unsafeRunSync())

  final override protected def withFixture(test: OneArgTest): Outcome = {
    def ftest(fix: FixtureParam): IO[Outcome] =
      for {
        _       <- logger.info(Map("test" -> s"'${test.name}'"))(s"INITIALIZED'")
        outcome <- IO.delay(test(fix))
        _       <- logger.info(Map("test" -> s"'${test.name}'", "outcome" -> outcome.productPrefix))(s"FINISHED")
      } yield outcome

    val fout: IO[Outcome] = fixture
      .onError {
        case e => Resource.liftF[IO, Unit](logger.warn(Map("test" -> s"'${test.name}'"), e)("INIT — FAILED"))
      }
      .use(ftest)

    fout.unsafeRunSync()
  }
}
