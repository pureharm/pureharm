package busymachines.pureharm.effects.pools

import cats.effect.{ContextShift, IO, Timer}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
object IORuntime {

  /**
    * Useful to create all needed machinery to properly work with
    * the cats-effect runtime.
    */
  def defaultMainRuntime(
    threadNamePrefix: String = "main-cpu-fixed",
  ): (ContextShift[IO], Timer[IO]) = {
    val ec = UnsafePools.defaultMainExecutionContext(threadNamePrefix)
    (mainIOContextShift(ec), mainIOTimer(ec))
  }

  /**
    * Use [[UnsafePools.defaultMainExecutionContext]], then pass it here, or use
    * [[defaultMainRuntime]] to instantiate all basic machinery.
    */
  def mainIOTimer(ec: ExecutionContextMainFT): Timer[IO] = IO.timer(ec)

  /**
    * Use [[UnsafePools.defaultMainExecutionContext]], then pass it here, or use
    * [[defaultMainRuntime]] to instantiate all basic machinery.
    */
  def mainIOContextShift(ec: ExecutionContextMainFT): ContextShift[IO] = IO.contextShift(ec)

  /**
    * Most likely you need only [[defaultMainRuntime]], because
    * there's little reason to expose the EC underlying your
    * [[ContextShift]] and [[Timer]].
    *
    * But in the cases you need that, this method behaves like
    * [[defaultMainRuntime]], but it also gives you the
    * underlying main thread pool
    */
  def defaultMainRuntimeWithEC(
    threadNamePrefix: String = "main-cpu-fixed",
  ): (ExecutionContextMainFT, ContextShift[IO], Timer[IO]) = {
    val ec = UnsafePools.defaultMainExecutionContext(threadNamePrefix)
    (ec, mainIOContextShift(ec), mainIOTimer(ec))
  }
}
