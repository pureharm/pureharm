package busymachines.pureharm.effects.pools.aliases

import busymachines.pureharm.effects.pools

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
trait PureharmPoolAliases {

  /**
    * Denotes execution contexts backed by a cached thread pool
    *
    * Can be constructed using or [[Pools]], [[UnsafePools]]
    */
  final type ExecutionContextCT = pools.ExecutionContextCT

  /**
    * Denotes execution contexts backed by a fixed thread pool
    *
    * Can be constructed using or [[Pools]], [[UnsafePools]]
    */
  final type ExecutionContextFT = pools.ExecutionContextFT

  /**
    * Denotes execution contexts with one single thread
    */
  final type ExecutionContextST = pools.ExecutionContextST

  /**
    * Similar to [[ExecutionContextFT]], except that it guarantees
    * that we have two threads, and it's specially designated
    * as the pool on which most (most of the time all) CPU bound
    * computation should be done in our apps, and the pool
    * underlying instances of [[cats.effect.ContextShift]] and
    * [[cats.effect.Timer]]
    *
    * Can be constructed using or [[UnsafePools]]
    */
  final type ExecutionContextMainFT = pools.ExecutionContextMainFT

  final val Pools:       pools.Pools.type       = pools.Pools
  final val UnsafePools: pools.UnsafePools.type = pools.UnsafePools

}
