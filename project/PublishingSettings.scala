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
  import bintray.BintrayKeys._
  
  /**
    * Publishing is done to bintray — to this org:
    * https://bintray.com/busymachines/maven-releases
    *
    * To set up bintray, just look at a combination of two docs:
    * https://github.com/sbt/sbt-bintray and a more user friendly:
    * http://queirozf.com/entries/publishing-an-sbt-project-onto-bintray-an-example
    *
    * TL;DR:
    * 1. create bintray account: https://bintray.com/
    * 2. request to be added as a member to busymachines organization : https://bintray.com/busymachines
    * 3. set your ~/.bintray/.credentials file to contain:
    * {{{
    * realm = Bintray API Realm
    * host = api.bintray.com
    * user = ${username that has publishing access to busymachines bintray org linked above}
    * password = ${get password by generating an API Key from the bintray UI}
    * }}}
    *
    * N.B. for historical purposes
    * For sync-ing to maven central you need to setup bintray to communicate via sonatype with
    * maven central. Quite complicated honestly. Should already be set-up at this point.
    * Anyway, the issue that gives you access to sonatype via which you can sync to maven central is here:
    * The username and password are the same as those to the Sonatype JIRA account.
    * https://issues.sonatype.org/browse/OSSRH-33718
    */
  object PublishingSettings {
  
    def bintraySettings: Seq[Setting[_]] = Seq(
      licenses                := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
      bintrayOrganization     := Some("busymachines"),
      bintrayRepository       := { if (isSnapshot.value) "maven-snapshots" else "maven-releases" },
      bintrayReleaseOnPublish := false,
      bintrayPackageLabels    := Seq("scala", "pureharm-aws", "busymachines"),
    )

    def noPublishSettings: Seq[Setting[_]] = Seq(
      publish                := {},
      publishLocal           := {},
      skip in publishLocal   := true,
      skip in publish        := true,
      skip in bintrayRelease := true,
      publishArtifact        := false,
    )

  }