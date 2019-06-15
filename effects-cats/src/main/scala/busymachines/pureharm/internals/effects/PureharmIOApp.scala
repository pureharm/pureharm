package busymachines.pureharm.internals.effects

import cats.Later
import cats.effect._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
trait PureharmIOApp extends IOApp {

  /**
    *
    * We want to ensure that this is evaluated only once, otherwise we introduce
    * too many possibilities of shooting oneself in the foot.
    *
    *
    * {{{
    * //created by mixing in the pureharm goodies
    * import myapp.effects._
    * //or, in lieu of that:
    * //import busymachines.pureharm.effects._
    *
    * object MyApp extends PureharmIOApp {
    *
    *   override ioRuntime: Later[(ContextShift[IO], Timer[IO])] =
    *     IORuntime.defaultMainRuntime("my-app")
    *
    *   //the usual... time to start instantiating all goodies
    *   def run(args: List[String]): IO[ExitCode] = IO.never[ExitCode]
    * }
    *
    * }}}
    *
    * @return
    */
  val ioRuntime: Later[(ContextShift[IO], Timer[IO])]

  /**
    *
    */
  final override protected def contextShift: ContextShift[IO] = ioRuntime.value._1

  /**
    * Removing implicitness brought in by [[IOApp]], to make it clearer
    * what's going on.
    */
  final override protected def timer: Timer[IO] = ioRuntime.value._2

}
