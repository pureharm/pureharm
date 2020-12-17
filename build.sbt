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

//#############################################################################
//#############################################################################
//#############################################################################

lazy val kernelVersion = "0.0.7-SNAPSHOT"
lazy val configVersion = "0.0.7-SNAPSHOT"
lazy val jsonVersion   = "0.0.7-SNAPSHOT"
lazy val dbVersion     = "0.0.7-SNAPSHOT"
lazy val restVersion   = "0.0.7-SNAPSHOT"

// format: off
addCommandAlias("ph-useScala213", s"++${CompilerSettings.scala2_13}")
addCommandAlias("ph-useScala30",  s"++${CompilerSettings.scala3_0}")

addCommandAlias("ph-publish-kernel-local", s"${publishAlias("kernel-publish")}")
addCommandAlias("ph-publish-config-local", s"${publishAlias("config")}")
addCommandAlias("ph-publish-json-local", s"${publishAlias("json-publish")}")
addCommandAlias("ph-publish-db-local", s"${publishAlias("db-publish")}")
addCommandAlias("ph-publish-rest-local", s"${publishAlias("rest-publish")}")

//setting the version because sonatypePublishBundle looks for a folder with the version in ThisBuild... for whatever reason...
addCommandAlias("ph-publish-kernel", s""";clean;set version in ThisBuild := "$kernelVersion";${publishAlias("kernel-publish")}""")
addCommandAlias("ph-publish-config", s""";clean;set version in ThisBuild := "$configVersion";${publishAlias("config")}""")
addCommandAlias("ph-publish-json", s""";clean;set version in ThisBuild := "$jsonVersion";${publishAlias("json-publish")}""")
addCommandAlias("ph-publish-db", s""";clean;set version in ThisBuild := "$dbVersion";${publishAlias("db-publish")}""")
addCommandAlias("ph-publish-rest", s""";clean;set version in ThisBuild := "$restVersion";${publishAlias("rest-publish")}""")

addCommandAlias("do213KernelRelease", ";ph-useScala213;ph-publish-kernel")
addCommandAlias("do30KernelRelease", ";ph-useScala30;ph-publish-kernel")
addCommandAlias("doKernelRelease", ";do213KernelRelease;do30KernelRelease")
addCommandAlias("doKernelLocal", ";ph-useScala213;ph-publish-kernel-local;ph-useScala30;ph-publish-kernel-local")

addCommandAlias("do213ConfigRelease", ";ph-useScala213;ph-publish-config")
addCommandAlias("do30ConfigRelease", ";ph-useScala30;ph-publish-config")
addCommandAlias("doConfigRelease", ";do213ConfigRelease;do30ConfigRelease")
addCommandAlias("doConfigLocal", ";ph-useScala213;ph-publish-config-local;ph-useScala30;ph-publish-config-local")

addCommandAlias("do213JsonRelease", ";ph-useScala213;ph-publish-json")
addCommandAlias("do30JsonRelease", ";ph-useScala30;ph-publish-json")
addCommandAlias("doJsonRelease", ";do213JsonRelease;do30JsonRelease")
addCommandAlias("doJsonLocal", ";ph-useScala213;ph-publish-json-local;ph-useScala30;ph-publish-json-local")

addCommandAlias("do213DBRelease", ";ph-useScala213;ph-publish-db")
addCommandAlias("do30DBRelease", ";ph-useScala30;ph-publish-db")
addCommandAlias("doDBRelease", ";do213DBRelease;do30DBRelease")
addCommandAlias("doDBLocal", ";ph-useScala213;ph-publish-db-local;ph-useScala30;ph-publish-db-local")

addCommandAlias("do213RestRelease", ";ph-useScala213;ph-publish-rest")
addCommandAlias("do30RestRelease", ";ph-useScala30;ph-publish-rest")
addCommandAlias("doRestRelease", ";do213RestRelease;do30RestRelease")
addCommandAlias("doRestLocal", ";ph-useScala213;ph-publish-rest-local;ph-useScala30;ph-publish-rest-local")

addCommandAlias("do213Release", ";ph-useScala213;ph-publish-kernel;ph-publish-config;ph-publish-json;ph-publish-db;ph-publish-rest")
addCommandAlias("do30Release", ";ph-useScala30;ph-publish-kernel;ph-publish-config;ph-publish-json;ph-publish-db;ph-publish-rest")

def rebuildAlias(mod: String): String = s"pureharm-$mod/clean;pureharm-$mod/compile;pureharm-$mod/Test/compile"
def localPublishAlias(mod: String): String = s"${rebuildAlias(mod)};pureharm-$mod/publishLocal"
def publishAlias(mod: String): String = s"${rebuildAlias(mod)};pureharm-$mod/publishSigned;sonatypeBundleRelease"
// format: on
//*****************************************************************************
//*****************************************************************************
//********************************* PROJECTS **********************************
//*****************************************************************************
//*****************************************************************************

lazy val root = Project(id = "pureharm", base = file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .aggregate(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `core`,
    `effects-cats`,
    testkit,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-flyway`,
    `db-core-psql`,
    `db-slick`,
    `db-doobie`,
    `db-testkit-core`,
    `db-testkit-doobie`,
    `db-test-data`,
    `db-testkit-slick`,
    `rest-http4s-tapir`,
    `rest-http4s-tapir-testkit`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ KERNEL +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//used only as a proxy for triggering publish of all kernel modules, does not contain source code.
lazy val `kernel-publish` = Project(id = s"pureharm-kernel-publish", base = file(s"kernel"))
  .settings(name := s"pureharm-kernel-publish", version := kernelVersion)
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .aggregate(
    `core`,
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `testkit`,
  )

lazy val `core` = kernelModule("core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
  )

//#############################################################################
lazy val `core-anomaly` = kernelModule("core-anomaly")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest % Test
    )
  )

//#############################################################################

lazy val `core-phantom` = kernelModule("core-phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies += (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => shapeless2
      case _             => shapeless3
    }),
    libraryDependencies ++= Seq(
      scalaTest % Test
    ),
  )

//#############################################################################

lazy val `core-identifiable` = kernelModule("core-identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies += (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => shapeless2
      case _             => shapeless3
    }),
    libraryDependencies ++= Seq(
      scalaTest % Test
    ),
  )
  .dependsOn(
    `core-phantom`
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ EFFECTS ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `effects-cats` = kernelModule("effects-cats")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      catsCore,
      catsEffect,
      scalaTest % Test,
    )
  )
  .dependsOn(
    `core-phantom`,
    `core-anomaly`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ TESTKIT ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `testkit` = kernelModule("testkit")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      log4cats,
      logbackClassic,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ JSON +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//used only as a proxy for triggering publish of all json modules, does not contain source code.
lazy val `json-publish` = Project(id = s"pureharm-json-publish", base = file(s"json"))
  .settings(name := s"pureharm-json-publish", version := jsonVersion)
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .aggregate(
    `json-circe`
  )

lazy val `json-circe` = jsonModule("circe")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      circeCore,
      circeGenericExtras,
      circeParser,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    asTestingLibrary(testkit),
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ CONFIG +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `config` = configModule
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      pureConfig
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    asTestingLibrary(testkit),
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ DB +++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//used only as a proxy for triggering publish of all db modules, does not contain source code.
lazy val `db-publish` = Project(id = s"pureharm-db-publish", base = file(s"db"))
  .settings(name := s"pureharm-db-publish", version := dbVersion)
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .aggregate(
    `db-core`,
    `db-core-psql`,
    `db-core-flyway`,
    `db-testkit-core`,
    `db-doobie`,
    `db-testkit-doobie`,
    `db-slick`,
    `db-testkit-slick`,
  )

lazy val `db-core` = dbModule("core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `config`,
  )

//#############################################################################

lazy val `db-core-psql` = dbModule("core-psql")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++=
      Seq(
        atto,
        postgresql,
      )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `config`,
    `db-core`,
    asTestingLibrary(testkit),
  )
//#############################################################################

lazy val `db-core-flyway` = dbModule("core-flyway")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      flyway
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `config`,
    `db-core`,
    asTestingLibrary(testkit),
  )

//#############################################################################

lazy val `db-testkit-core` = dbModule("testkit-core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      logbackClassic,
      log4cats,
      scalaTest,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `config`,
    `db-core`,
    `db-core-flyway`,
    testkit,
  )
//#############################################################################

//used only in the interior of pureharm to test the implementations!
lazy val `db-test-data` = dbModule("test-data")
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `config`,
    `db-core`,
    `db-core-flyway`,
    `db-testkit-core`,
  )

//#############################################################################
lazy val `db-doobie` = dbModule("doobie")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      doobieCore,
      doobieHikari,
      doobiePSQL,
      postgresql,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-psql`,
  )

//#############################################################################

lazy val `db-testkit-doobie` = dbModule("testkit-doobie")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `config`,
    `db-core`,
    `db-core-flyway`,
    `db-doobie`,
    testkit,
    `db-testkit-core`,
    asTestingLibrary(`db-test-data`),
  )

//#############################################################################

lazy val `db-slick` = dbModule("slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      slick,
      hikari,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-psql`,
  )

//#############################################################################

//#############################################################################

lazy val `db-testkit-slick` = dbModule("testkit-slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      slickTestkit
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-psql`,
    `db-slick`,
    testkit,
    `db-testkit-core`,
    asTestingLibrary(`db-test-data`),
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ HTTP ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//used only as a proxy for triggering publish of all db modules, does not contain source code.
lazy val `rest-publish` = Project(id = s"pureharm-rest-publish", base = file(s"rest"))
  .settings(name := s"pureharm-rest-publish", version := restVersion)
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .aggregate(
    `rest-http4s-tapir`,
    `rest-http4s-tapir-testkit`,
  )

lazy val `rest-http4s-tapir` = restModule("http4s-tapir")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      http4sDSL,
      http4sCirce,
      http4sServer,
      tapirCore,
      tapirCirce,
      tapirHttp4s,
      tapirOpenAPI,
      tapirCirceYAML,
    )
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `json-circe`,
  )

//#############################################################################

lazy val `rest-http4s-tapir-testkit` = restModule("http4s-tapir-testkit")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
    `json-circe`,
    `rest-http4s-tapir`,
    testkit,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val shapeless2Version: String = "2.4.0-M1"  //https://github.com/milessabin/shapeless/releases
lazy val shapeless3Version: String = "3.0.0-M1"  //https://github.com/milessabin/shapeless/releases
lazy val catsVersion:       String = "2.3.0"     //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion: String = "2.3.0"     //https://github.com/typelevel/cats-effect/releases
lazy val fs2Version:        String = "2.4.6"     //https://github.com/functional-streams-for-scala/fs2/releases
lazy val circeVersion:      String = "0.13.0"    //https://github.com/circe/circe/releases
lazy val pureconfigVersion: String = "0.14.0"    //https://github.com/pureconfig/pureconfig/releases
lazy val attoVersion:       String = "0.8.0"     //https://github.com/tpolecat/atto/releases
lazy val slickVersion:      String = "3.3.3"     //https://github.com/slick/slick/releases
lazy val postgresqlVersion: String = "42.2.18"   //java — https://github.com/pgjdbc/pgjdbc/releases
lazy val hikariCPVersion:   String = "3.4.5"     //java — https://github.com/brettwooldridge/HikariCP/releases
lazy val doobieVersion:     String = "0.10.0-M2" //https://github.com/tpolecat/doobie/releases
lazy val flywayVersion:     String = "7.3.1"     //java — https://github.com/flyway/flyway/releases
lazy val log4catsVersion:   String = "1.1.1"     //https://github.com/ChristopherDavenport/log4cats/releases
lazy val logbackVersion:    String = "1.2.3"     //https://github.com/qos-ch/logback/releases
lazy val http4sVersion:     String = "0.21.14"   //https://github.com/http4s/http4s/releases
lazy val tapirVersion:      String = "0.17.0"    //https://github.com/softwaremill/tapir/releases
lazy val scalaTestVersion:  String = "3.2.3"     //https://github.com/scalatest/scalatest/releases

//=============================================================================
//================================= TYPELEVEL =================================
//=============================================================================

//https://github.com/typelevel/cats/releases
lazy val catsCore: ModuleID = "org.typelevel" %% "cats-core" % catsVersion withSources ()

//https://github.com/typelevel/cats-effect/releases
lazy val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources ()

//https://github.com/circe/circe/releases
lazy val circeCore:          ModuleID = "io.circe" %% "circe-core"           % circeVersion withSources ()
lazy val circeGenericExtras: ModuleID = "io.circe" %% "circe-generic-extras" % circeVersion withSources ()
lazy val circeParser:        ModuleID = "io.circe" %% "circe-parser"         % circeVersion withSources ()

//https://github.com/milessabin/shapeless/releases
lazy val shapeless2: ModuleID = "com.chuusai" %% "shapeless" % shapeless2Version withSources ()
lazy val shapeless3: ModuleID = "com.chuusai" %% "shapeless" % shapeless3Version withSources ()

//https://github.com/functional-streams-for-scala/fs2/releases
lazy val fs2: ModuleID = "co.fs2" %% "fs2-core" % fs2Version withSources ()

//=============================================================================
//================================= DATABASE ==================================
//=============================================================================

//https://github.com/brettwooldridge/HikariCP/releases
lazy val hikari: ModuleID = "com.zaxxer" % "HikariCP" % hikariCPVersion withSources ()

//https://github.com/flyway/flyway/releases
lazy val flyway: ModuleID = "org.flywaydb" % "flyway-core" % flywayVersion withSources ()

//https://github.com/pgjdbc/pgjdbc/releases
lazy val postgresql: ModuleID = "org.postgresql" % "postgresql" % postgresqlVersion withSources ()

//=============================================================================
//============================= DATABASE - DOOBIE =============================
//=============================================================================

//https://github.com/tpolecat/doobie/releases
lazy val doobieCore   = "org.tpolecat" %% "doobie-core"     % doobieVersion withSources ()
lazy val doobieHikari = "org.tpolecat" %% "doobie-hikari"   % doobieVersion withSources ()
lazy val doobiePSQL   = "org.tpolecat" %% "doobie-postgres" % doobieVersion withSources ()

//=============================================================================
//============================= DATABASE - SLICK ==============================
//=============================================================================

//https://github.com/slick/slick/releases
lazy val slick:        ModuleID = "com.typesafe.slick" %% "slick"         % slickVersion withSources ()
lazy val slickTestkit: ModuleID = "com.typesafe.slick" %% "slick-testkit" % slickVersion withSources ()

//=============================================================================
//=========================== HTTP - HTTP4S + TAPIR ===========================
//=============================================================================
lazy val http4sDSL    = "org.http4s" %% "http4s-dsl"          % http4sVersion withSources ()
lazy val http4sCirce  = "org.http4s" %% "http4s-circe"        % http4sVersion withSources ()
lazy val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion withSources ()

lazy val tapirCore   = "com.softwaremill.sttp.tapir" %% "tapir-core"          % tapirVersion withSources ()
lazy val tapirCirce  = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % tapirVersion withSources ()
lazy val tapirHttp4s = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion withSources ()

//for testing
lazy val tapirOpenAPI   = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion withSources ()
lazy val tapirCirceYAML = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion withSources ()

//=============================================================================
//================================== TESTING ==================================
//=============================================================================

//https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestVersion withSources ()

//=============================================================================
//================================== HELPERS ==================================
//=============================================================================

lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % pureconfigVersion withSources ()
lazy val atto:       ModuleID = "org.tpolecat"          %% "atto-core"  % attoVersion       withSources ()
//=============================================================================
//=================================  LOGGING ==================================
//=============================================================================
//https://github.com/ChristopherDavenport/log4cats/releases
lazy val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion withSources ()

//https://github.com/qos-ch/logback/releases — it is the backend implementation used by log4cats-slf4j
lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion withSources ()

//=============================================================================
//================================  BUILD UTILS ===============================
//=============================================================================
/** See SBT docs:
  * https://www.scala-sbt.org/release/docs/Multi-Project.html#Per-configuration+classpath+dependencies
  *
  * Ensures dependencies between the ``test`` parts of the modules
  */
def fullDependency(p: Project): ClasspathDependency = p % "compile->compile;test->test"

/** See SBT docs:
  * https://www.scala-sbt.org/release/docs/Multi-Project.html#Per-configuration+classpath+dependencies
  *
  * or an example:
  * {{{
  * val testModule = project
  *
  * val prodModule = project
  *   .dependsOn(asTestingLibrary(testModule))
  * }}}
  * To ensure that testing code and dependencies
  * do not wind up in the "compile" (i.e.) prod part of your
  * application.
  */
def asTestingLibrary(p: Project): ClasspathDependency = p % "test -> compile"

def kernelModule(mod: String): Project =
  Project(id = s"pureharm-$mod", base = file(s"kernel/$mod"))
    .settings(name := s"pureharm-$mod", version := kernelVersion)

def jsonModule(mod:   String): Project =
  Project(id = s"pureharm-json-$mod", base = file(s"json/$mod"))
    .settings(name := s"pureharm-json-$mod", version := jsonVersion)

def configModule: Project =
  Project(id = s"pureharm-config", base = file(s"config"))
    .settings(name := s"pureharm-config", version := configVersion)

def dbModule(mod:     String): Project =
  Project(id = s"pureharm-db-$mod", base = file(s"db/$mod"))
    .settings(name := s"pureharm-db-$mod", version := dbVersion)

def restModule(mod:   String): Project =
  Project(id = s"pureharm-rest-$mod", base = file(s"rest/$mod"))
    .settings(name := s"pureharm-rest-$mod", version := restVersion)
