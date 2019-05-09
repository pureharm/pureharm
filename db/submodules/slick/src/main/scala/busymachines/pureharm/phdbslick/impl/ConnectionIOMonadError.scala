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
package busymachines.pureharm.phdbslick.impl

import busymachines.pureharm.db.ConnectionIOEC
import busymachines.pureharm.phdbslick.slickTypes._

import busymachines.pureharm.effects._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
private[phdbslick] class ConnectionIOMonadError(implicit ec: ConnectionIOEC) extends MonadError[ConnectionIO, Throwable] {
  override def pure[A](x: A): ConnectionIO[A] = ConnectionIO.successful(x)

  override def map[A, B](fa: ConnectionIO[A])(f: A => B): ConnectionIO[B] = fa.map(f)

  override def flatMap[A, B](fa: ConnectionIO[A])(f: A => ConnectionIO[B]): ConnectionIO[B] = fa.flatMap(f)

  override def tailRecM[A, B](a: A)(f: A => ConnectionIO[Either[A, B]]): ConnectionIO[B] = f(a).flatMap {
    case Left(a1) => tailRecM(a1)(f)
    case Right(b) => ConnectionIO.successful(b)
  }

  override def raiseError[A](e: Throwable): ConnectionIO[A] = ConnectionIO.failed(e)

  override def handleError[A](fea: ConnectionIO[A])(f: Throwable => A): ConnectionIO[A] =
    fea.asTry.map {
      case TrySuccess(v) => v
      case TryFailure(t) => f(t)
    }

  override def handleErrorWith[A](fa: ConnectionIO[A])(f: Throwable => ConnectionIO[A]): ConnectionIO[A] =
    fa.asTry.flatMap {
      case TrySuccess(v) => pure(v)
      case TryFailure(t) => f(t)
    }

  override def attempt[A](fa: ConnectionIO[A]): ConnectionIO[Either[Throwable, A]] = fa.asTry.map {
    case TrySuccess(v) => Right[Throwable, A](v)
    case TryFailure(t) => Left[Throwable, A](t)
  }
}
