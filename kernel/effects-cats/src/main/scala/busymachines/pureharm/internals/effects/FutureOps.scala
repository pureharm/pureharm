/** Copyright (c) 2019 BusyMachines
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

import scala.concurrent.duration._
import busymachines.pureharm.effects._

import scala.collection.BuildFrom

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 May 2019
  */
private[internals] object FutureOps {
  private val unitFunction: Any => Unit = _ => ()

  @inline def void(f: Future[_])(implicit ec: ExecutionContext): Future[Unit] = f.map(unitFunction)

  /** See [[Await.result]]
    */
  @inline def unsafeRunSync[A](f: Future[A], atMost: Duration = Duration.Inf): A = Await.result(f, atMost)

  //=========================================================================
  //=============================== Traversals ==============================
  //=========================================================================

//  /**
//    *
//    * Similar to [[scala.concurrent.Future.traverse]], but discards all content. i.e. used only
//    * for the combined effects.
//    *
//    * @see [[scala.concurrent.Future.traverse]]
//    */
//  @inline def traverse_[A, B, M[X] <: IterableOnce[X]](
//    in: M[A],
//  )(fn: A => Future[B])(implicit bf: BuildFrom[M[A], B, M[B]], executor: ExecutionContext): Future[Unit] =
//    Future.traverse(in)(fn).map(_ => ())

  /** Similar to [[scala.concurrent.Future.traverse]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.traverse]]
    */
  @inline def traverse_[A, B](
    in: Seq[A]
  )(fn: A => Future[B])(implicit executor: ExecutionContext): Future[Unit] =
    this.void(Future.traverse(in)(fn))

  /** Similar to [[scala.concurrent.Future.traverse]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.traverse]]
    */
  @inline def traverse_[A, B](
    in: Set[A]
  )(fn: A => Future[B])(implicit executor: ExecutionContext): Future[Unit] =
    this.void(Future.traverse(in)(fn))

  //the generic version gets: Cannot construct a collection of type M[A] with elements of type A based on a collection of type M[scala.concurrent.Future[A]]
  //on scala 2.12... so much for compatibility, lol
//  /**
//    *
//    * Similar to [[scala.concurrent.Future.sequence]], but discards all content. i.e. used only
//    * for the combined effects.
//    *
//    * @see [[scala.concurrent.Future.sequence]]
//    */
//  @inline def sequence_[A, M[X] <: IterableOnce[X], To](
//    in:          M[Future[A]],
//  )(implicit bf: BuildFrom[M[Future[A]], A, To], executor: ExecutionContext): Future[Unit] =
//    this.void(Future.sequence(in))

  /** Similar to [[scala.concurrent.Future.sequence]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.sequence]]
    */
  @inline def sequence_[A, To](
    in:                Seq[Future[A]]
  )(implicit executor: ExecutionContext): Future[Unit] =
    this.void(Future.sequence(in))

  /** Similar to [[scala.concurrent.Future.sequence]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.sequence]]
    */
  @inline def sequence_[A, To](
    in:                Set[Future[A]]
  )(implicit executor: ExecutionContext): Future[Unit] =
    this.void(Future.sequence(in))

  /** Syntactically inspired from [[Future.traverse]], but it differs semantically
    * insofar as this method does not attempt to run any futures in parallel. "M" stands
    * for "monadic", as opposed to "applicative" which is the foundation for the formal definition
    * of "traverse" (even though in Scala it is by accident-ish)
    *
    * For the vast majority of cases you should prefer this method over [[Future.sequence]]
    * and [[Future.traverse]], since even small collections can easily wind up queuing so many
    * [[Future]]s that you blow your execution context.
    *
    * Usage:
    * {{{
    *   import busymachines.effects.async._
    *   val patches: Seq[Patch] = //...
    *
    *   //this ensures that no two changes will be applied in parallel.
    *   val allPatches: Future[Seq[Patch]] = Future.serialize(patches){ patch: Patch =>
    *     Future {
    *       //apply patch
    *     }
    *   }
    *   //... and so on, and so on!
    * }}}
    */
  @inline def serialize[A, B, M[X] <: IterableOnce[X]](
    in: M[A]
  )(fn: A => Future[B])(implicit bf: BuildFrom[M[A], B, M[B]], executor: ExecutionContext): Future[M[B]] = {
    import scala.collection.mutable
    if (in.iterator.isEmpty) {
      Future.successful(bf.newBuilder(in).result())
    }
    else {
      val seq  = in.iterator.to(Seq)
      val head = seq.head
      val tail = seq.tail
      val builder: mutable.Builder[B, M[B]] = bf.newBuilder(in)
      val firstBuilder = fn(head).map(z => builder.+=(z))
      val eventualBuilder: Future[mutable.Builder[B, M[B]]] = tail.foldLeft(firstBuilder) {
        (serializedBuilder: Future[mutable.Builder[B, M[B]]], element: A) =>
          serializedBuilder.flatMap[mutable.Builder[B, M[B]]] { (result: mutable.Builder[B, M[B]]) =>
            val f: Future[mutable.Builder[B, M[B]]] = fn(element).map(newElement => result.+=(newElement))
            f
          }
      }
      eventualBuilder.map(b => b.result())
    }
  }

  /** @see [[serialize]]
    *
    * Similar to [[serialize]], but discards all content. i.e. used only
    * for the combined effects.
    */
  @inline def serialize_[A, B, M[X] <: IterableOnce[X]](
    in: M[A]
  )(fn: A => Future[B])(implicit bf: BuildFrom[M[A], B, M[B]], executor: ExecutionContext): Future[Unit] =
    this.void(FutureOps.serialize(in)(fn))
}
