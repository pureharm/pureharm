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
package busymachines.pureharm.internals.effects

import scala.concurrent.duration._
import scala.language.postfixOps

import busymachines.pureharm.internals.effects.types.{Attempt, MonadAttempt}
import cats._
import cats.effect._
import cats.implicits._
/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  */

final class PureharmTimedAttemptReattemptSyntaxOps[F[_], A](val fa: F[A]) extends AnyVal {

  /** @param unit
    *   You probably don't want a granularity larger than [[MILLISECONDS]]
    *   for accurate timing.
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *  Never fails and captures the failure of the ``fa`` within the [[Attempt]],
    *  times both success and failure case.
    */
  def timedAttempt(
    unit:       TimeUnit = MILLISECONDS
  )(implicit F: MonadAttempt[F], timer: Timer[F]): F[(FiniteDuration, Attempt[A])] =
    PureharmTimedAttemptReattemptSyntaxOps.timedAttempt(unit)(fa)

  /** Runs an effect ``F[A]`` a maximum of ``retries`` time, until
    * it is not failed. Between each retry it waits ``betweenRetries``.
    * It also measures the time elapsed in total.
    *
    * @param errorLog
    *   Use this to specify how to log any error that happens within
    *   your ``fa``, and any error encountered during retries
    * @param timeUnit
    *   You probably don't want a granularity larger than [[MILLISECONDS]]
    *   for accurate timing.
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *  Never fails and captures the failure of the ``fa`` within the [[Attempt]],
    *  times all successes and failures, and returns their sum.
    *  N.B.
    *  It only captures the latest failure, if it encounters one.
    */
  def timedReattempt(
    errorLog:       (Throwable, String) => F[Unit],
    timeUnit:       TimeUnit,
  )(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(implicit
    F:              Sync[F],
    timer:          Timer[F],
  ): F[(FiniteDuration, Attempt[A])] =
    PureharmTimedAttemptReattemptSyntaxOps.timedReattempt(errorLog, timeUnit)(retries, betweenRetries)(fa)

  /** Same as overload [[timedReattempt]], but does not report any failures.
    */
  def timedReattempt(
    timeUnit:       TimeUnit
  )(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(implicit
    F:              Sync[F],
    timer:          Timer[F],
  ): F[(FiniteDuration, Attempt[A])] =
    PureharmTimedAttemptReattemptSyntaxOps
      .timedReattempt(PureharmTimedAttemptReattemptSyntaxOps.noLog[F], timeUnit)(retries, betweenRetries)(fa)

  /** Runs an effect ``F[A]`` a maximum of ``retries`` time, until
    * it is not failed. Between each retry it waits ``betweenRetries``.
    * It also measures the time elapsed in total.
    *
    * @param errorLog
    *   Use this to specify how to log any error that happens within
    *   your ``fa``, and any error encountered during retries
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *   N.B.
    *   It only captures the latest failure, if it encounters one.
    */
  def reattempt(
    errorLog:       (Throwable, String) => F[Unit]
  )(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(implicit
    F:              Sync[F],
    timer:          Timer[F],
  ): F[A] =
    PureharmTimedAttemptReattemptSyntaxOps.reattempt(errorLog)(retries, betweenRetries)(fa)

  /** Same semantics as overload [[reattempt]]but does not report any error
    */
  def reattempt(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(implicit
    F:              Sync[F],
    timer:          Timer[F],
  ): F[A] =
    PureharmTimedAttemptReattemptSyntaxOps.reattempt(retries, betweenRetries)(fa)
}

object PureharmTimedAttemptReattemptSyntaxOps {
  import scala.concurrent.duration._

  trait Implicits {

    implicit def pureharmTimedAttemptReattemptSyntaxOPS[F[_], A](
      fa: F[A]
    ): PureharmTimedAttemptReattemptSyntaxOps[F, A] =
      new PureharmTimedAttemptReattemptSyntaxOps[F, A](fa)

  }

  /** @param timeUnit
    *   You probably don't want a granularity larger than [[MILLISECONDS]]
    *   for accurate timing.
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *  Never fails and captures the failure of the ``fa`` within the [[Attempt]],
    *  times both success and failure case.
    */
  def timedAttempt[F[_], A](
    timeUnit:   TimeUnit
  )(
    fa:         F[A]
  )(implicit F: MonadAttempt[F], timer: Timer[F]): F[(FiniteDuration, Attempt[A])] =
    for {
      start <- realTime(timeUnit)(F, timer)
      att   <- fa.attempt
      end   <- realTime(timeUnit)(F, timer)
    } yield (end.minus(start), att)

  /** Runs an effect ``F[A]`` a maximum of ``retries`` time, until
    * it is not failed. Between each retry it waits ``betweenRetries``.
    * It also measures the time elapsed in total.
    *
    * @param errorLog
    *   Use this to specify how to log any error that happens within
    *   your ``fa``, and any error encountered during retries
    * @param timeUnit
    *   You probably don't want a granularity larger than [[MILLISECONDS]]
    *   for accurate timing.
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *  Never fails and captures the failure of the ``fa`` within the [[Attempt]],
    *  times all successes and failures, and returns their sum.
    *  N.B.
    *  It only captures the latest failure, if it encounters one.
    */
  def timedReattempt[F[_]: Sync: Timer, A](
    errorLog:       (Throwable, String) => F[Unit],
    timeUnit:       TimeUnit,
  )(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(
    fa:             F[A]
  ): F[(FiniteDuration, Attempt[A])] = {
    def recursiveRetry(fa: F[A])(rs: Int, soFar: FiniteDuration): F[(FiniteDuration, Attempt[A])] =
      for {
        timedAtt <- this.timedAttempt[F, A](timeUnit)(fa)
        newSoFar = soFar.plus(timedAtt._1)
        v <- timedAtt._2 match {
          case Right(v) => (newSoFar, v.pure[Attempt]).pure[F]
          case Left(e)  =>
            if (rs > 0) {
              for {
                _     <- errorLog(
                  e,
                  s"effect failed: ${e.getMessage}. retries left=$rs, but first waiting: $betweenRetries",
                )
                _     <- Timer[F].sleep(betweenRetries)
                value <- recursiveRetry(fa)(rs - 1, newSoFar.plus(betweenRetries))
              } yield value: (FiniteDuration, Attempt[A])
            }
            else {
              errorLog(e, s"all retries failed, abandoning.") >>
                (newSoFar, e.raiseError[Attempt, A]).pure[F]
            }
        }
      } yield v

    recursiveRetry(fa)(retries, 0 seconds)
  }

  /** Runs an effect ``F[A]`` a maximum of ``retries`` time, until
    * it is not failed. Between each retry it waits ``betweenRetries``.
    * It also measures the time elapsed in total.
    *
    * @param errorLog
    *   Use this to specify how to log any error that happens within
    *   your ``fa``, and any error encountered during retries
    *
    *   N.B. you can also use [[FiniteDuration.toCoarsest]] to then obtain
    *   a more human friendly measurement as possible
    * @return
    *   N.B.
    *   It only captures the latest failure, if it encounters one.
    */
  def reattempt[F[_]: Sync: Timer, A](
    errorLog:       (Throwable, String) => F[Unit]
  )(
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(
    fa:             F[A]
  ): F[A] =
    this.timedReattempt(errorLog, NANOSECONDS)(retries, betweenRetries)(fa).map(_._2).rethrow

  /** Same semantics as overload [[reattempt]]but does not report any error
    */
  def reattempt[F[_]: Sync: Timer, A](
    retries:        Int,
    betweenRetries: FiniteDuration,
  )(
    fa:             F[A]
  ): F[A] =
    this.timedReattempt(noLog(Sync[F]), NANOSECONDS)(retries, betweenRetries)(fa).map(_._2).rethrow

  private def noLog[F[_]: Applicative]: (Throwable, String) => F[Unit] =
    (_, _) => Applicative[F].unit

  //find appropriate util package for this... looks useful...
  private def realTime[F[_]: Applicative: Timer](unit: TimeUnit): F[FiniteDuration] =
    Timer[F].clock.realTime(unit).map(tl => FiniteDuration(tl, unit))

}
