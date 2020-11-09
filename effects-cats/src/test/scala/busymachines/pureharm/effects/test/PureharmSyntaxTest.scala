/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.effects.test

import org.scalatest.funspec.AnyFunSpec
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 May 2019
  */
final class PureharmSyntaxTest extends AnyFunSpec {
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

    test("flatten") {
      assert(pure.flattenOption(ifEmpty) == F.pure(value))
      assert(fail.flattenOption(ifEmpty) == F.raiseError(ifEmpty))
    }

    test("ifSomeRaise") {
      assert(pure.ifSomeRaise(ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise(ifEmpty) == F.unit)
    }

    test("ifSomeRaise(A => Throwable)") {
      assert(pure.ifSomeRaise((_: Int) => ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise((_: Int) => ifEmpty) == F.unit)
    }

    test("ifNoneRun") {
      assert(pure.ifNoneRun(F.raiseError(ifEmpty)) == F.unit)
      assert(fail.ifNoneRun(F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
    }

    test("ifSomeRun") {
      assert(pure.ifSomeRun(F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun(F.raiseError(ifEmpty)) == F.unit)
    }

    test("ifSomeRun(A => Throwable)") {
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

    test("ifSomeRaise") {
      assert(pure.ifSomeRaise[F](ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise[F](ifEmpty) == F.unit)
    }

    test("ifSomeRaise(A => Throwable)") {
      assert(pure.ifSomeRaise[F]((_: Int) => ifEmpty) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRaise[F]((_: Int) => ifEmpty) == F.unit)
    }

    test("ifNoneRun") {
      assert(pure.ifNoneRun[F](F.raiseError(ifEmpty)) == F.unit)
      assert(fail.ifNoneRun[F](F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
    }

    test("ifSomeRun") {
      assert(pure.ifSomeRun[F](F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun[F](F.raiseError(ifEmpty)) == F.unit)
    }

    test("ifSomeRun(A => Throwable)") {
      assert(pure.ifSomeRun[F]((_: Int) => F.raiseError(ifEmpty)) == F.raiseError(ifEmpty))
      assert(fail.ifSomeRun[F]((_: Int) => F.raiseError(ifEmpty)) == F.unit)
    }

  }

  describe("Attempt[_] syntax") {
    val value = 42

    val err = new RuntimeException("attempt—syntax")
    test("onError") {
      val pure: Attempt[Int] = Attempt.pure(value)
      val fail: Attempt[Int] = Attempt.raiseError(err)

      withClue("pure") {
        var state = 1
        val io: SyncIO[Int] = pure.onErrorF(SyncIO { state = value })
        assert(io.unsafeRunSync() == value)
        assert(state == 1, "state should not change")
      }

      withClue("fail") {
        var state = 2
        val io: SyncIO[Int] = fail.onErrorF(SyncIO { state = value })
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

    test("ifFalseRaise") {
      assert(trueF.ifFalseRaise(failure) == F.unit, "trueF")
      assert(falseF.ifFalseRaise(failure) == F.raiseError(failure), "falseF")
    }

    test("ifTrueRaise") {
      assert(trueF.ifTrueRaise(failure) == F.raiseError(failure), "trueF")
      assert(falseF.ifTrueRaise(failure) == F.unit, "falseF")
    }

    test("ifFalseRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueF.ifFalseRun(F.raiseError(newFail)) == F.unit, "trueF")
      assert(falseF.ifFalseRun(F.raiseError(newFail)) == F.raiseError(newFail), "falseF")
    }

    test("ifTrueRun") {
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

    test("ifFalseRaise") {
      assert(trueV.ifFalseRaise[F](failure) == F.unit, "trueF")
      assert(falseV.ifFalseRaise[F](failure) == F.raiseError(failure), "falseF")
    }

    test("ifTrueRaise") {
      assert(trueV.ifTrueRaise[F](failure) == F.raiseError(failure), "trueF")
      assert(falseV.ifTrueRaise[F](failure) == F.unit, "falseF")
    }

    test("ifFalseRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueV.ifFalseRun(F.raiseError(newFail)) == F.unit, "trueF")
      assert(falseV.ifFalseRun(F.raiseError(newFail)) == F.raiseError(newFail), "falseF")
    }

    test("ifTrueRun") {
      val newFail = new RuntimeException("other branch!")
      assert(trueV.ifTrueRun(F.raiseError(newFail)) == F.raiseError(newFail), "trueF")
      assert(falseV.ifTrueRun(F.raiseError(newFail)) == F.unit, "falseF")
    }
  }

  describe("Any F syntax") {
    val value = 42

    test("onError(fu: F[_])") {
      val failure = new RuntimeException("failure")
      val pure    = IO.pure(value)
      val fail    = IO.raiseError(failure)

      withClue("... pure") {
        var state = 1
        val io    = pure.onErrorF(IO { state = value })
        assert(io.unsafeRunSync() == value)
        assert(state == 1, "state should not change")
      }

      withClue("... fail") {
        var state = 1
        val io    = fail.onErrorF(IO { state = value })
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
              previouslyProcessed.foreach { previous =>
                assertResult(expected = i - 1, "... the futures were not executed in the correct order.")(
                  actual = previous
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
              previouslyProcessed.foreach { previous =>
                assertResult(expected = i - 1, "... the futures were not executed in the correct order.")(
                  actual = previous
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

    describe("liftTo[...]") {
      implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

      test("... IO") {
        var sideEffect: Int     = 0
        val io:         IO[Int] = Future[Int] {
          sideEffect = 42
          sideEffect
        }.liftTo[IO]
        // we wait because in case this doesn't work,
        // the Future is already running and doing all its side-effects
        Thread.sleep(100)
        if (sideEffect == 42) fail("side effect should not have been applied yet")
        assert(io.unsafeRunSync() == 42, "value of suspended future")
        assert(sideEffect == 42, "after unsafeRunSync side effect should be applied")
      }

      test("... final tagless F") {
        var sideEffect: Int = 0

        def testPureSuspension[F[_]: Async: ContextShift]: F[Int] = {
          val f: F[Int] = Future[Int] {
            sideEffect = 42
            sideEffect
          }.liftTo[F]
          // we wait because in case this doesn't work,
          // the Future is already running and doing all its side-effects
          Thread.sleep(100)
          if (sideEffect == 42) fail("side effect should not have been applied yet")

          f
        }

        val io = testPureSuspension[IO]
        assert(io.unsafeRunSync() == 42, "value of suspended future")
        assert(sideEffect == 42, "after unsafeRunSync side effect should be applied")
      }
    }
  }

  describe("IO syntax") {
    describe("traversals") {

      describe("IO.serialize") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[List[Unit]] = IO.serialize(input) { _ =>
            IO {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("IO.serialize_") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[Unit] = IO.serialize_(input) { _ =>
            IO {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("IO.traverse") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[List[Unit]] = IO.traverse(input) { _ =>
            IO {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("IO.traverse_") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[Unit] = IO.traverse_(input) { _ =>
            IO {
              sideEffect = 42
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("IO.sequence") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[List[Unit]] = IO.sequence {
            input.map { _ =>
              IO {
                sideEffect = 42
              }
            }
          }

          eventualResult.unsafeRunSync()
          assert(sideEffect == 0, "nothing should have happened")
        }
      }

      describe("IO.sequence_") {

        test("empty list") {
          val input: List[Int] = List()

          var sideEffect: Int = 0

          val eventualResult: IO[Unit] = IO.sequence_ {
            input.map { _ =>
              IO {
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
