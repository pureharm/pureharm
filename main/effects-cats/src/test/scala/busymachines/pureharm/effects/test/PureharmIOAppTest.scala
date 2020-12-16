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
package busymachines.pureharm.effects.test

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/** This is an example of "main" method, built using pureharm.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
object PureharmIOAppTest extends PureharmIOApp {

  /** We want to ensure that this is evaluated only once, otherwise we introduce
    * too many possibilities of shooting oneself in the foot.
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
  override val ioRuntime: Later[(ContextShift[IO], Timer[IO])] =
    IORuntime.defaultMainRuntime("pureharm-test-app")

  override def run(args: List[String]): IO[ExitCode] = {
    //using ``ConcurrentEffect`` because it's the strongest typeclass,
    //and is the one that requires backing by a context shift. But in your
    //own app don't be too overeager to actually use it, you might not even
    //need it. Had we preserved the explicit contextShift this would not be
    //needed, but it's only one single line that elucidates where the typeclass
    //comes from. Often a big hurdle for beginners
    implicit val F:   ConcurrentEffect[IO] = IO.ioConcurrentEffect(contextShift)
    implicit val cs:  ContextShift[IO]     = contextShift
    implicit val tim: Timer[IO]            = timer
    val res = appResource[IO](ConcurrentEffect[IO], contextShift)
    res
      .use(runApp[IO])
      .as(ExitCode.Success)
      .handleErrorWith(e => putStrLn(s"FAILED: $e") >> IO.pure(ExitCode.Error))
  }

  private def runApp[F[_]: Sync: Timer](res: AppResource[F])(implicit p: Parallel[F]): F[Unit] = {
    import scala.concurrent.duration._
    for {
      _ <- (List.range[Int](1, 100): List[Int])
        .parTraverse_ { i: Int =>
          val toWait = 400.millis
          val fa     = getThreadName[F] >>= { tn =>
            putStrLn[F](s"$i going to sleep on thread: $tn") >>
              Timer[F].sleep(toWait) >>
              putStrLn[F](s"$i waking up on thread: $tn")
          }

          val faBlocking = getThreadName[F] >>= { tn =>
            putStrLn[F](s"$i going to sleep on thread: $tn") >>
              Sync[F].delay(Thread.sleep(toWait.toMillis)) >>
              putStrLn[F](s"$i waking up on thread: $tn")
          }
          if (i % 2 == 0) fa else res.blockingShifter.blockOn(faBlocking): F[Unit]
        }
    } yield ()
  }

  private def appResource[F[_]](implicit
    F:  Sync[F],
    cs: ContextShift[F],
  ): Resource[F, AppResource[F]] =
    for {
      blockingCT <- Pools.cached[F]("pureharm-blocking")
    } yield new AppResource[F](
      blockingShifter = BlockingShifter.fromExecutionContext[F](blockingCT)
    )

  final private class AppResource[F[_]](
    val blockingShifter: BlockingShifter[F]
  )

  private def putStrLn[F[_]: Sync](s: String): F[Unit] = Sync[F].delay(println(s))

  private def getThreadName[F[_]: Sync]: F[String] = Sync[F].delay(Thread.currentThread().getName)
}
