package busymachines.pureharm.effects.pools

import busymachines.pureharm.effects.pools

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
trait PoolAliases {

  /**
    * Denotes executions contexts backed by a fixed thread pool
    *
    * Can be constructed using or [[Pools]], [[UnsafePools]]
    */
  final type ExecutionContextFT = pools.ExecutionContextFT

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

  /**
    * Denotes executions contexts backed by a cached thread pool
    *
    * Can be constructed using or [[Pools]], [[UnsafePools]]
    */
  final type ExecutionContextCT = pools.ExecutionContextCT

  final val Pools:       pools.Pools.type       = pools.Pools
  final val UnsafePools: pools.UnsafePools.type = pools.UnsafePools

}
