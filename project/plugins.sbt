/** Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
/** Helps us publish the artifacts to sonatype, which in turn
  * pushes to maven central. Please follow instructions of setting
  * up as described in:
  * http://busymachines.github.io/busymachines-commons/docs/publishing-artifacts.html
  *
  * https://github.com/xerial/sbt-sonatype/releases
  */
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5") //https://github.com/xerial/sbt-sonatype/releases

/** Signs all the jars, used in conjunction with sbt-sonatype.
  *
  * Do not forget to include this in your global plugins as described in:
  * http://busymachines.github.io/busymachines-commons/docs/publishing-artifacts.html
  *
  * https://github.com/sbt/sbt-pgp/releases
  */
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1") //https://github.com/sbt/sbt-pgp/releases

/** build configured in ``project/ReleaseProcess``
  *
  * https://github.com/sbt/sbt-release/releases
  */
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13") //https://github.com/sbt/sbt-release/releases

/** The best thing since sliced bread.
  *
  * https://github.com/scalameta/sbt-scalafmt/releases
  */
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2") //https://github.com/scalameta/sbt-scalafmt/releases

/** Refactoring/linting tool for scala.
  *
  * https://github.com/scalacenter/scalafix/releases
  * https://scalacenter.github.io/scalafix/
  *
  * From docs:
  * {{{
  *   // ===> sbt shell
  *
  *   > scalafixEnable           // Setup scalafix for active session. — enabling by default slows down compilation
  *
  *   > scalafix                 // Run all rules configured in .scalafix.conf
  *
  *   > scalafix OrganizeImports // Run only RemoveUnusedImports rule
  *
  *   > myProject/scalafix      // Run rule in one project only
  *
  * }}}
  */
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.23") //https://github.com/scalacenter/scalafix/releases

/** https://github.com/lampepfl/dotty/releases
  */
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.5")
