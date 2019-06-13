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
  "FutureLift needs a bunch of machinery. Make sure you don't have two implicit scala.concurrent.ExecutionContext, or two cats.effect.ContextShift[F] in scope, or two LiftIO[F]",
)
@implicitNotFound(
  "FutureLift can only be instantiated for ContextShift[IO] for F[_]: LiftIO. You should instantiate one of these in your main, and propagate it further",
)
trait FutureLift[F[_]] {
  def fromFuture[A](fut: => Future[A]): F[A]
}

object FutureLift {

  def apply[F[_]](implicit fl: FutureLift[F]): FutureLift[F] = fl

  import cats.effect._

  implicit def instance[F[_]: LiftIO](implicit cs: ContextShift[IO]): FutureLift[F] = new FutureLiftIO[F]

  final private[FutureLift] class FutureLiftIO[F[_]](
    implicit
    private val cs:     ContextShift[IO],
    private val liftIO: LiftIO[F],
  ) extends FutureLift[F] {
    override def fromFuture[A](fut: => Future[A]): F[A] = IO.fromFuture(IO(fut)).to[F]
  }
}
