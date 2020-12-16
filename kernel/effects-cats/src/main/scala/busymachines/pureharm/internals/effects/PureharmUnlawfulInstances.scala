package busymachines.pureharm.internals.effects

import cats._
import cats.implicits._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 23 Jun 2020
  */
trait PureharmUnlawfulInstances {

  implicit val pureharmUnlawfulTraverseIterable: Traverse[Iterable] = new Traverse[Iterable] {

    override def foldLeft[A, B](fa: Iterable[A], b: B)(f: (B, A) => B): B =
      fa.foldLeft(b)(f)

    override def foldRight[A, B](fa: Iterable[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.foldRight(lb)(f)

    override def traverse[G[_], A, B](fa: Iterable[A])(f: A => G[B])(implicit G: Applicative[G]): G[Iterable[B]] = {
      import scala.collection.mutable
      foldLeft[A, G[mutable.Builder[B, mutable.Iterable[B]]]](fa, G.pure(mutable.Iterable.newBuilder[B]))((lglb, a) =>
        G.map2(f(a), lglb)((b: B, lb: mutable.Builder[B, mutable.Iterable[B]]) => lb.+=(b))
      ).map(_.result().toIterable)
    }
  }
}
