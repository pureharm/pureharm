package busymachines.pureharm.internals.effects

import cats.effect.{ContextShift, IO}

import scala.concurrent.ExecutionContext

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jun 2019
  *
  */
object PoolUtils {

  def mainContextShift(threadNamePrefix: String = "main-cpu-fixed"): ContextShift[IO] =
    IO.contextShift(mainContextShiftPool(threadNamePrefix))

  def mainContextShiftPool(threadNamePrefix: String = "main-cpu-fixed"): ExecutionContext =
    MainCPUPool.default(threadNamePrefix)
}
