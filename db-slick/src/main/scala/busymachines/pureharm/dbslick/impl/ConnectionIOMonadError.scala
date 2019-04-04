package busymachines.pureharm.dbslick.impl

import cats._

import busymachines.pureharm.dbslick.ConnectionIO

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
private[dbslick] class ConnectionIOMonadError(implicit ec: ExecutionContext)
    extends MonadError[ConnectionIO, Throwable] {
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
      case Success(v) => v
      case Failure(t) => f(t)
    }

  override def handleErrorWith[A](fa: ConnectionIO[A])(f: Throwable => ConnectionIO[A]): ConnectionIO[A] =
    fa.asTry.flatMap {
      case Success(v) => pure(v)
      case Failure(t) => f(t)
    }

  override def attempt[A](fa: ConnectionIO[A]): ConnectionIO[Either[Throwable, A]] = fa.asTry.map {
    case Success(v) => Right[Throwable, A](v)
    case Failure(t) => Left[Throwable,  A](t)
  }
}
