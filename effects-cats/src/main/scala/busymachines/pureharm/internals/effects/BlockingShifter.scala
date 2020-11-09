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

import busymachines.pureharm.effects._

/** Used to block on an F[A], and ensure that all recovery and
  * shifting back is always done.
  *
  * For instance, always ensure that any F[A] that
  * talks to, say, amazon S3, is wrapped in such
  * a
  * {{{
  *   blockingShifter.blockOn(S3Util.putSomething(...))
  * }}}
  *
  * Libraries in the typelevel eco-system tend to already do
  * this, so you don't need to be careful. For instance,
  * doobie will always ensure that this is done to and
  * from the EC that you provide specifically for accessing the
  * DB. But you always need to double check, and be careful
  * that you NEVER execute blocking IO on the same thread pool
  * as the CPU bound one dedicated to your ContextShift[A]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
sealed trait BlockingShifter[F[_]] {
  def contextShift: ContextShift[F]

  def blocker: Blocker

  final def delay[A](thunk: => A)(implicit F: Sync[F]): F[A] =
    blocker.delay[F, A](thunk)(F, contextShift)

  final def blockOn[A](fa: F[A]): F[A] =
    blocker.blockOn(fa)(contextShift)
}

object BlockingShifter {

  def apply[F[_]](implicit summon: BlockingShifter[F]): BlockingShifter[F] = summon

  /** See [[Pools.cached]] on where to get a proper thread pool
    */
  def fromExecutionContext[F[_]: ContextShift](ec: ExecutionContextCT): BlockingShifter[F] =
    new BlockingShifterImpl(ContextShift[F], Blocker.liftExecutionContext(ec))

  /** Unsafe because you should really instantiate this using a cached thread pool.
    *
    * Use this unless you really know what you are doing. Otherwise prefer [[fromExecutionContext]]
    */
  def unsafeFromExecutionContext[F[_]: ContextShift](ec: ExecutionContext): BlockingShifter[F] =
    new BlockingShifterImpl(ContextShift[F], Blocker.liftExecutionContext(ec))

  /** Prefer [[fromExecutionContext]], unless you know for certain that this
    * blocker was instantiated with the proper underlying execution context.
    */
  def unsafeFromBlocker[F[_]: ContextShift](blocker: Blocker): BlockingShifter[F] =
    new BlockingShifterImpl(ContextShift[F], blocker)

  final private class BlockingShifterImpl[F[_]](
    override val contextShift: ContextShift[F],
    override val blocker:      Blocker,
  ) extends BlockingShifter[F]
}
