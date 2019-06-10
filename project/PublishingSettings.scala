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
import sbt._
import Keys._
import com.typesafe.sbt.SbtPgp.autoImportImpl._
import xerial.sbt.Sonatype.SonatypeKeys._

/**
  * All instructions for publishing to sonatype can be found on the sbt-plugin page:
  * http://www.scala-sbt.org/release/docs/Using-Sonatype.html
  *
  * and some here:
  *
  * https://github.com/xerial/sbt-sonatype
  *
  * VERY IMPORTANT!!! You need to add credentials as described here:
  * http://www.scala-sbt.org/release/docs/Using-Sonatype.html#Second+-+Configure+Sonatype+integration
  *
  * The username and password are the same as those to the Sonatype JIRA account.
  * https://issues.sonatype.org/browse/OSSRH-33718
  *
  * And then you need to ensure PGP keys to sign the maven artifacts:
  * http://www.scala-sbt.org/release/docs/Using-Sonatype.html#PGP+Tips%E2%80%99n%E2%80%99tricks
  * Which means that you need to have PGP installed
  *
  * To avoid pushing sensitive information to github you must put all PGP related things in your local configs:
  * as described here:
  *
  * https://github.com/sbt/sbt-pgp/issues/69
  */
object PublishingSettings {

  def sonatypeSettings: Seq[Setting[_]] = Seq(
    useGpg                     := true,
    sonatypeProfileName        := Settings.organizationName,
    publishArtifact in Compile := true,
    publishArtifact in Test    := false,
    publishMavenStyle          := true,
    pomIncludeRepository       := (_ => false),
    publishTo := Option {
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    },
    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scmInfo := Option(
      ScmInfo(
        url("https://github.com/busymachines/pureharm"),
        "scm:git@github.com:busymachines/pureharm.git",
      ),
    ),
    developers := List(
      Developer(
        id    = "lorandszakacs",
        name  = "Lorand Szakacs",
        email = "lorand.szakacs@busymachines.com",
        url   = url("https://github.com/lorandszakacs"),
      ),
    ),
  )

  def noPublishSettings: Seq[Setting[_]] = Seq(
    publish              := {},
    publishLocal         := {},
    skip in publishLocal := true,
    skip in publish      := true,
    publishArtifact      := false,
  )

}
