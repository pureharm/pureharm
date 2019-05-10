package busymachines.pureharm.effects_impl

import org.scalatest.FunSpec

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 May 2019
  *
  */
final class PureharmSyntaxTest extends FunSpec {
  private def test: ItWord = it

  describe("F[Option[_]] syntax") {
    //makes testing more concise and easier to change
    val F = Attempt
    val G = Option
    type F[+A] = Attempt[A]
    type G[+A] = Option[A]

    val value: Int       = 42
    val pure:  F[G[Int]] = F.pure(G(value))
    val fail:  F[G[Int]] = F.pure(G.empty)

    val ifEmpty = new RuntimeException("for empty option")

    it("... flatten") {
      assert(pure.flatten(ifEmpty) == F.pure(value))
      assert(fail.flatten(ifEmpty) == F.raiseError(ifEmpty))
    }

    it("... ifSomeRaise") {
      assert(pure.ifSomeRaise(ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise(ifEmpty) == F.unit)
    }

    it("... ifSomeRaise(A => Throwable)") {
      assert(pure.ifSomeRaise((_: Int) => ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise((_: Int) => ifEmpty) == F.unit)
    }

    it("... ifNoneRun") {
      assert(pure.ifNoneRun(F.raiseError(ifEmpty)) == F.unit)
      assert(fail.ifNoneRun(F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
    }

    it("... ifSomeRun") {
      assert(pure.ifSomeRun(F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun(F.raiseError(ifEmpty)) == F.unit)
    }

    it("... ifSomeRun(A => Throwable)") {
      assert(pure.ifSomeRun(_ => F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun(_ => F.raiseError(ifEmpty)) == F.unit)
    }

  }

  describe("Option[_] syntax") {
    //makes testing more concise and easier to change
    //makes testing more concise and easier to change
    val F = Attempt
    val G = Option

    type F[+A] = Attempt[A]
    type G[+A] = Option[A]

    val value: Int    = 42
    val pure:  G[Int] = G(value)
    val fail:  G[Int] = G.empty

    val ifEmpty = new RuntimeException("for empty option")

    it("... ifSomeRaise") {
      assert(pure.ifSomeRaise[F](ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise[F](ifEmpty) == F.unit)
    }

    it("... ifSomeRaise(A => Throwable)") {
      assert(pure.ifSomeRaise[F]((_: Int) => ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise[F]((_: Int) => ifEmpty) == F.unit)
    }

    it("... ifNoneRun") {
      assert(pure.ifNoneRun[F](F.raiseError(ifEmpty)) == F.unit)
      assert(fail.ifNoneRun[F](F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
    }

    it("... ifSomeRun") {
      assert(pure.ifSomeRun[F](F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun[F](F.raiseError(ifEmpty)) == F.unit)
    }

    it("... ifSomeRun(A => Throwable)") {
      assert(pure.ifSomeRun[F]((_: Int) => F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun[F]((_: Int) => F.raiseError(ifEmpty)) == F.unit)
    }

  }

  describe("Attempt[_] syntax") {
    val value = 42

    val err = new RuntimeException("attempt—syntax")
    it("... onError") {
      val pure: Attempt[Int] = Attempt.pure(value)
      val fail: Attempt[Int] = Attempt.raiseError(err)

      withClue("pure") {
        var state = 1
        val io: SyncIO[Int] = pure.onError(SyncIO { state = value })
        assert(io.unsafeRunSync() == value)
        assert(state == 1, "state should not change")
      }

      withClue("fail") {
        var state = 2
        val io: SyncIO[Int] = fail.onError(SyncIO { state = value })
        assert(io.attempt.unsafeRunSync() == fail)
        assert(state == value, "state should change")
      }
    }

  }

  describe("F[Boolean] syntax") {
    //makes testing more concise and easier to change
    val F = Attempt
    type F[+A] = Attempt[A]

    val trueF:  F[Boolean] = F.pure(true)
    val falseF: F[Boolean] = F.pure(false)

    val failure = new RuntimeException("boolean throwable")

    it("... ifFalseRaise") {
      assert(trueF.ifFalseRaise(failure) == F.unit, "trueF")
      assert(falseF.ifFalseRaise(failure) == F.raiseError(failure), "falseF")
    }

    it("... ifTrueRaise") {
      assert(trueF.ifTrueRaise(failure) == F.raiseError(failure), "trueF")
      assert(falseF.ifTrueRaise(failure) == F.unit, "falseF")
    }

    it("... ifFalseRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueF.ifFalseRun(F.raiseError(newFail)) == F.unit, "trueF")
      assert(falseF.ifFalseRun(F.raiseError(newFail)) == F.raiseError(newFail), "falseF")
    }

    it("... ifTrueRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueF.ifTrueRun(F.raiseError(newFail)) == F.raiseError(newFail), "trueF")
      assert(falseF.ifTrueRun(F.raiseError(newFail)) == F.unit, "falseF")
    }
  }

  describe("Boolean syntax") {
    //makes testing more concise and easier to change
    val F = Attempt
    type F[+A] = Attempt[A]

    val trueV:  Boolean = true
    val falseV: Boolean = false

    val failure = new RuntimeException("boolean throwable")

    it("... ifFalseRaise") {
      assert(trueV.ifFalseRaise[F](failure) == F.unit, "trueF")
      assert(falseV.ifFalseRaise[F](failure) == F.raiseError(failure), "falseF")
    }

    it("... ifTrueRaise") {
      assert(trueV.ifTrueRaise[F](failure) == F.raiseError(failure), "trueF")
      assert(falseV.ifTrueRaise[F](failure) == F.unit, "falseF")
    }

    it("... ifFalseRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueV.ifFalseRun(F.raiseError(newFail)) == F.unit, "trueF")
      assert(falseV.ifFalseRun(F.raiseError(newFail)) == F.raiseError(newFail), "falseF")
    }

    it("... ifTrueRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueV.ifTrueRun(F.raiseError(newFail)) == F.raiseError(newFail), "trueF")
      assert(falseV.ifTrueRun(F.raiseError(newFail)) == F.unit, "falseF")
    }
  }

  describe("Any F syntax") {
    val value = 42

    it("... onError(fu: F[_])") {
      val failure = new RuntimeException("failure")
      val pure    = IO.pure(value)
      val fail    = IO.raiseError(failure)

      withClue("... pure") {
        var state = 1
        val io    = pure.onError(IO { state = value })
        assert(io.unsafeRunSync() == value)
        assert(state == 1, "state should not change")
      }

      withClue("... fail") {
        var state = 1
        val io    = fail.onError(IO { state = value })
        assert(io.attempt.unsafeRunSync() == Attempt.raiseError(failure))
        assert(state == value, "state should change")
      }

    }
  }

  describe("Future syntax") {
    implicit val ec: ExecutionContext = ExecutionContext.global

    describe("traversals") {

      describe("Future.serialize") {

        test("empty list") {
          val input:    Seq[Int] = List()
          val expected: Seq[Int] = List()

          var sideEffect: Int = 0

          val eventualResult = Future.serialize(input) { _ =>
            Future[Int] {
              sideEffect = 42
              sideEffect
            }
          }

          assert(eventualResult.unsafeRunSync() == expected)
          assert(sideEffect == 0, "nothing should have happened")
        }

        test("no two futures should run in parallel") {
          val input: Seq[Int] = (1 to 100).toList
          val expected = input.map(_.toString)

          var previouslyProcessed: Option[Int] = None
          var startedFlag:         Option[Int] = None

          val eventualResult: Future[Seq[String]] = Future.serialize(input) { i =>
            Future {
              assert(
                startedFlag.isEmpty,
                s"started flag should have been empty at the start of each future but was: $startedFlag",
              )
              previouslyProcessed foreach { previous =>
                assertResult(expected = i - 1, "... the futures were not executed in the correct order.")(
                  actual = previous,
                )
              }
              startedFlag         = Some(i)
              startedFlag         = None
              previouslyProcessed = Some(i)
              i.toString
            }
          }
          assert(expected == eventualResult.unsafeRunSync())
          assert(previouslyProcessed == Option(100))
        }
      }

      describe("Future.serialize_") {

        test("empty list") {
          val input: Seq[Int] = List()

          var sideEffect: Int = 0

          val eventualResult = Future.serialize_(input) { _ =>
            Future {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }

        test("no two futures should run in parallel") {
          val input: Seq[Int] = (1 to 100).toList

          var previouslyProcessed: Option[Int] = None
          var startedFlag:         Option[Int] = None

          val eventualResult: Future[Unit] = Future.serialize_(input) { i =>
            Future {
              assert(
                startedFlag.isEmpty,
                s"started flag should have been empty at the start of each future but was: $startedFlag",
              )
              previouslyProcessed foreach { previous =>
                assertResult(expected = i - 1, "... the futures were not executed in the correct order.")(
                  actual = previous,
                )
              }
              startedFlag         = Some(i)
              startedFlag         = None
              previouslyProcessed = Some(i)
              i.toString
            }
          }
          eventualResult.unsafeRunSync()
          assert(previouslyProcessed == Option(100))
        }
      }

      describe("Future.traverse_") {

        test("empty list") {
          val input: Seq[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: Future[Unit] = Future.traverse_(input) { _ =>
            Future {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("Future.sequence_") {

        test("empty list") {
          val input: Seq[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: Future[Unit] = Future.sequence_ {
            input.map { _ =>
              Future {
                sideEffect = 42
              }
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }
    }
  }
}
