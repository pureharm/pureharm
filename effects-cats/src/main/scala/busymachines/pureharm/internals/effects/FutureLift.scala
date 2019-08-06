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
package busymachines.pureharm.internals.effects

import scala.annotation.{implicitAmbiguous, implicitNotFound}
import scala.concurrent.Future

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
@implicitAmbiguous(
  "There are several cases where this can go wrong:\n\t1) you have two ContextShift[F] and/or two Async[F] instances in scope.\n\t2)You have one ContextShift[F] and one Async[F], AND one FutureLift[F] in scope.",
)
@implicitNotFound(
  "Future lift can be  instantiated if for any F[_] for which you have an implicit ContextShift[F] AND an implicit Async[F] in scope",
)
@scala.deprecated(
  "Since cats-effect 2.0.0-M5 there exists Async[F].fromFuture. Prefer using that one instead. FutureLift will be removed in 0.0.3",
  "0.0.2-M17",
)
trait FutureLift[F[_]] {
  def fromFuture[A](fut: => Future[A]): F[A]
}

object FutureLift {

  def apply[F[_]](implicit fl: FutureLift[F]): FutureLift[F] = fl

  import cats.effect._

  @scala.deprecated(
    "Since cats-effect 2.0.0-M5 there exists Async[F].fromFuture. And a a FutureLift.polymorphicFutureLift[F] constructor, you can also use that.",
    "0.0.2-M17",
  )
  def instance[F[_]: LiftIO](implicit cs: ContextShift[IO]): FutureLift[F] = new FutureLiftIO[F]

  def polymorphicFutureLift[F[_]: ContextShift](implicit F: Async[F]): FutureLift[F] = new FutureLiftF[F]

  final private[FutureLift] class FutureLiftF[F[_]](
    implicit
    private val F:  Async[F],
    private val cs: ContextShift[F],
  ) extends FutureLift[F] {
    override def fromFuture[A](fut: => Future[A]): F[A] = Async.fromFuture(F.delay(fut))

  }

  @scala.deprecated(
    "Since cats-effect 2.0.0-M5 there exists Async[F].fromFuture. And a a FutureLift.polymorphicFutureLift[F] constructor, you can also use that.",
    "0.0.2-M17",
  )
  final private[FutureLift] class FutureLiftIO[F[_]](
    implicit
    private val cs:     ContextShift[IO],
    private val liftIO: LiftIO[F],
  ) extends FutureLift[F] {
    override def fromFuture[A](fut: => Future[A]): F[A] = IO.fromFuture(IO(fut)).to[F]
  }
}
