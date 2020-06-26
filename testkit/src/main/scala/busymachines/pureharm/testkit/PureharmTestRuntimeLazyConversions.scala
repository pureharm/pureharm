package busymachines.pureharm.testkit

import busymachines.pureharm.effects._

/**
  *
  * Basically the problem is as such:
  * 1) ideally, you probably want only one [[PureharmTestRuntime]] instantiated in your entire test run
  *    to not waste resources and startup time...
  * 2) and you want easy usage...
  *
  * To achieve 2) we could make it a val runtime, and do import runtime._ in the tests, and all would
  * be good!
  *
  * But then 1) is impossible...
  *
  * By doing this trick the runtime is instantiated as late as possible, and you get
  * all the necessary imports in your tests usage as well!
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  *
  */
trait PureharmTestRuntimeLazyConversions {
  implicit def phrtToEC(implicit phtr: PureharmTestRuntime): ExecutionContext    = phtr.executionContextCT
  implicit def phrtToCS(implicit phtr: PureharmTestRuntime): ContextShift[IO]    = phtr.contextShift
  implicit def phrtToTM(implicit phtr: PureharmTestRuntime): Timer[IO]           = phtr.timer
  implicit def phrtToBS(implicit phtr: PureharmTestRuntime): BlockingShifter[IO] = phtr.blockingShifter
  implicit def phrtToBL(implicit phtr: PureharmTestRuntime): Blocker             = phtr.blocker
}
