/**
  * Copyright (c) 2019 BusyMachines
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
/**
  * Helps us publish the artifacts to sonatype, which in turn
  * pushes to maven central. Please follow instructions of setting
  * up as described in:
  * http://busymachines.github.io/busymachines-commons/docs/publishing-artifacts.html
  *
  * https://github.com/xerial/sbt-sonatype
  */
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.4")

/**
  *
  * Signs all the jars, used in conjunction with sbt-sonatype.
  *
  * Do not forget to include this in your global plugins as described in:
  * http://busymachines.github.io/busymachines-commons/docs/publishing-artifacts.html
  *
  * https://github.com/sbt/sbt-pgp
  */
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0-M2")

/**
  * build configured in ``project/ReleaseProcess``
  *
  * https://github.com/sbt/sbt-release
  */
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")

/**
  * The best thing since sliced bread.
  *
  * https://github.com/scalameta/scalafmt
  */
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "1.5.1")

/**
  * Refactoring/linting tool for scala.
  *
  * https://github.com/scalacenter/scalafix
  * https://scalacenter.github.io/scalafix/
  *
  * From docs:
  * {{{
  *   // ===> sbt shell
  *
  *   > scalafixEnable                         // Setup scalafix for active session.
  *
  *   > scalafix                               // Run all rules configured in .scalafix.conf
  *
  *   > scalafix RemoveUnusedImports           // Run only RemoveUnusedImports rule
  *
  *   > myProject/scalafix RemoveUnusedImports // Run rule in one project only
  *
  * }}}
  */
//addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.6.0-M1")

/**
  * Used to build the documentation.
  *
  * https://github.com/47deg/sbt-microsites
  */
addSbtPlugin("com.47deg" % "sbt-microsites" % "0.7.18")

/**
  *
  * Used by sbt-microsites
  *
  * https://github.com/sbt/sbt-ghpages
  */
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2")

/**
  * https://github.com/scoverage/sbt-scoverage
  */
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
