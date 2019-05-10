package busymachines.pureharm.effects_impl

import scala.concurrent.duration._
import busymachines.pureharm.effects._

import scala.collection.generic.CanBuildFrom
/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 May 2019
  *
  */
private[effects_impl] object FutureOps {
  private val unitFunction: Any => Unit = _ => ()

  @inline def void(f: Future[_])(implicit ec: ExecutionContext): Future[Unit] = f.map(unitFunction)

  /**
    * See [[Await.result]]
    */
  @inline def unsafeRunSync[A](f: Future[A], atMost: Duration = Duration.Inf): A = Await.result(f, atMost)

  //=========================================================================
  //=============================== Traversals ==============================
  //=========================================================================

  /**
    *
    * Similar to [[scala.concurrent.Future.traverse]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.traverse]]
    */
  @inline def traverse_[A, B, M[X] <: TraversableOnce[X]](in: M[A])(fn: A => Future[B])(
    implicit
    cbf: CanBuildFrom[M[A], B, M[B]],
    ec:  ExecutionContext,
  ): Future[Unit] = this.void(Future.traverse(in)(fn))

  /**
    *
    * Similar to [[scala.concurrent.Future.sequence]], but discards all content. i.e. used only
    * for the combined effects.
    *
    * @see [[scala.concurrent.Future.sequence]]
    */
  @inline def sequence_[A, M[X] <: TraversableOnce[X]](in: M[Future[A]])(
    implicit
    cbf: CanBuildFrom[M[Future[A]], A, M[A]],
    ec:  ExecutionContext,
  ): Future[Unit] = this.void(Future.sequence(in))

  /**
    *
    * Syntactically inspired from [[Future.traverse]], but it differs semantically
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
    *
    *
    */
  @inline def serialize[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Future[B])(
    implicit
    cbf: CanBuildFrom[C[A], B, C[B]],
    ec:  ExecutionContext,
  ): Future[C[B]] = {
    import scala.collection.mutable
    if (col.isEmpty) {
      Future.successful(cbf.apply().result())
    }
    else {
      val seq  = col.toSeq
      val head = seq.head
      val tail = seq.tail
      val builder: mutable.Builder[B, C[B]] = cbf.apply()
      val firstBuilder = fn(head) map { z =>
        builder.+=(z)
      }
      val eventualBuilder: Future[mutable.Builder[B, C[B]]] = tail.foldLeft(firstBuilder) {
        (serializedBuilder: Future[mutable.Builder[B, C[B]]], element: A) =>
          serializedBuilder.flatMap[mutable.Builder[B, C[B]]] { (result: mutable.Builder[B, C[B]]) =>
            val f: Future[mutable.Builder[B, C[B]]] = fn(element) map { newElement =>
              result.+=(newElement)
            }
            f
          }
      }
      eventualBuilder map { b =>
        b.result()
      }
    }
  }

  /**
    * @see [[serialize]]
    *
    * Similar to [[serialize]], but discards all content. i.e. used only
    * for the combined effects.
    */
  @inline def serialize_[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Future[B])(
    implicit
    cbf: CanBuildFrom[C[A], B, C[B]],
    ec:  ExecutionContext,
  ): Future[Unit] = this.void(FutureOps.serialize(col)(fn))
}
