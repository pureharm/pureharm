package busymachines.pureharm.effects.test

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.duration._

final class PureharmReattemptTest extends AnyFunSuite {

  implicit private val cs:    ContextShift[IO] = IO.contextShift(scala.concurrent.ExecutionContext.global)
  implicit private val timer: Timer[IO]        = IO.timer(scala.concurrent.ExecutionContext.global)

  implicit private val blockingShifter: BlockingShifter[IO] =
    BlockingShifter.unsafeFromExecutionContext[IO](scala.concurrent.ExecutionContext.global)

  test("reattempt — succeed on first try") {
    val f = for {
      firstTry <- Ref.of[IO, Int](0)
      _        <- succeedAfter(firstTry).reattempt(retries = 3, betweenRetries = 10.millis)
    } yield ()
    f.unsafeRunSync()
  }

  test("reattempt — succeed after more than one try") {
    val f = for {
      threeTries <- Ref.of[IO, Int](3)
      _          <- succeedAfter(threeTries).reattempt(retries = 3, betweenRetries = 10.millis)
    } yield ()
    f.unsafeRunSync()
  }

  test("reattempt — fail after more than one try") {
    val f = for {
      threeTries <- Ref.of[IO, Int](3)
      attempted  <- succeedAfter(threeTries).reattempt(retries = 2, betweenRetries = 10.millis).attempt
      _ = attempted match {
        case Left(e)  => assert(e.getMessage == "failed 1")
        case Right(_) => fail("expected failure")
      }
    } yield ()
    f.unsafeRunSync()
  }

  private def succeedAfter(tries: Ref[IO, Int]): IO[Unit] =
    for {
      _          <- BlockingShifter[IO].blockOn(IO(Thread.sleep(10.millis.toMillis)))
      triesSoFar <- tries.getAndUpdate(soFar => soFar - 1)
      _          <- IO(println(s"try countdown: #$triesSoFar"))
      _          <-
        if (triesSoFar <= 0) {
          IO.unit
        }
        else
          IO.raiseError(new RuntimeException(s"failed $triesSoFar"))
    } yield ()

}
