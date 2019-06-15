package busymachines.pureharm.effects

import busymachines.pureharm.phantom.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
package object pools {
  private[pools] object ExecutionContextFT extends PhantomType[ExecutionContext]

  /**
    * Denotes executions contexts backed by a fixed thread pool
    */
  type ExecutionContextFT = ExecutionContextFT.Type

  private[pools] object ExecutionContextCT extends PhantomType[ExecutionContext]

  /**
    * Denotes executions contexts backed by a cached thread pool
    */
  type ExecutionContextCT = ExecutionContextCT.Type

  private[pools] object ExecutionContextMainFT extends PhantomType[ExecutionContext]
  type ExecutionContextMainFT = ExecutionContextMainFT.Type
}
