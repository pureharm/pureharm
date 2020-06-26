package busymachines.pureharm

import busymachines.pureharm.effects._
import busymachines.pureharm.phantom.PhantomType
import io.chrisdavenport.log4cats._

import scala.annotation.implicitNotFound

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  *
  */
package object testkit {

  @implicitNotFound(
    """
      |TestLogger is implemented by default in either:
      |  - busymachines.pureharm.testkit.FixturePureharmTest
      |  - busymachines.pureharm.testkit.PureharmTest
      | 
      |Depending on which one you use. And there is little reason to want
      |it from somewhere else. But you can always override it to plug
      |in a custom one.
      |
      |The purpose of TestLogger is to log everything related to test-setup/
      |tear-down to enrich whatever scalatest tells you
      |
      |""".stripMargin
  )
  type TestLogger = TestLogger.Type

  object TestLogger extends PhantomType[StructuredLogger[IO]] {

    def fromClass(cl: Class[_]): TestLogger =
      this.apply(slf4j.Slf4jLogger.getLoggerFromName[IO](s"${cl.getCanonicalName}.test.report"))
  }

}
