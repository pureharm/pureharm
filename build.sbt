/**
  * Copyright (c) 2019 BusyMachines
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

//see: https://github.com/liancheng/scalafix-organize-imports
//and the project-specific config in .scalafix.conf
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.0"

// format: off
addCommandAlias("useScala212", s"++${CompilerSettings.scala2_12}")
addCommandAlias("useScala213", s"++${CompilerSettings.scala2_13}")
addCommandAlias("useDotty",    s"++${CompilerSettings.dottyVersion}")

addCommandAlias("recompile",      ";clean;compile;")
addCommandAlias("build",          ";compile;Test/compile")
addCommandAlias("rebuild",        ";clean;compile;Test/compile")
addCommandAlias("rebuild-update", ";clean;update;compile;Test/compile")
addCommandAlias("ci",             ";scalafmtCheck;rebuild-update;test")
addCommandAlias("ci-quick",       ";scalafmtCheck;build;test")
addCommandAlias("doLocal",        ";clean;update;compile;publishLocal")

addCommandAlias("cleanPublishSigned", ";recompile;publishSigned")
addCommandAlias("do212Release",       ";useScala212;cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("do213Release",       ";useScala213;cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("doRelease",          ";do212Release;do213Release")

addCommandAlias("lint", ";scalafixEnable;rebuild;scalafix;scalafmtAll")
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
    core,
    `effects-cats`,
    testkit,
    time,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-flyway`,
    `db-core-psql`,
    `db-slick`,
    `db-slick-psql`,
    `db-doobie`,
    `db-testkit-core`,
    `db-testkit-doobie`,
    `db-test-data`,
    `db-testkit-slick`,
    `rest-http4s-tapir`,
    `rest-http4s-tapir-testkit`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ CORE ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val core = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-core",
    libraryDependencies ++= Nil,
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
  )

//#############################################################################

lazy val `core-anomaly` = subModule("core", "anomaly")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest.withDottyCompat(scalaVersion.value) % Test
    )
  )

//#############################################################################

lazy val `core-phantom` = subModule("core", "phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless.withDottyCompat(scalaVersion.value),
      scalaTest.withDottyCompat(scalaVersion.value) % Test,
    )
  )

//#############################################################################

lazy val `core-identifiable` = subModule("core", "identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless.withDottyCompat(scalaVersion.value),
      scalaTest.withDottyCompat(scalaVersion.value) % Test,
    )
  )
  .dependsOn(
    `core-phantom`
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ EFFECTS ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `effects-cats` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-effects-cats",
    libraryDependencies ++= Seq(
      catsCore.withDottyCompat(scalaVersion.value),
      catsEffect.withDottyCompat(scalaVersion.value),
      scalaCollectionCompat.withDottyCompat(scalaVersion.value),
      scalaTest.withDottyCompat(scalaVersion.value) % Test,
    ),
  )
  .dependsOn(
    `core-phantom`,
    `core-anomaly`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ TIME +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `time` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-time",
    libraryDependencies ++= Seq(),
  )
  .dependsOn(
    `core-phantom`,
    `core-anomaly`,
    `effects-cats`,
    asTestingLibrary(testkit),
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ JSON +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `json-circe` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-json-circe",
    libraryDependencies ++= Seq(
      circeCore.withDottyCompat(scalaVersion.value),
      circeGenericExtras.withDottyCompat(scalaVersion.value),
      circeParser.withDottyCompat(scalaVersion.value),
    ),
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

lazy val `config` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-config",
    libraryDependencies ++= Seq(
      shapeless.withDottyCompat(scalaVersion.value),
      pureConfig.withDottyCompat(scalaVersion.value),
    ),
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

lazy val `db-core` = subModule("db", "core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
  )

//#############################################################################

lazy val `db-core-psql` = subModule("db", "core-psql")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++=
      Seq(
        atto.withDottyCompat(scalaVersion.value),
        postgresql,
      )
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
    asTestingLibrary(testkit),
  )
//#############################################################################

lazy val `db-core-flyway` = subModule("db", "core-flyway")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      flyway
    )
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
    asTestingLibrary(testkit),
  )

//#############################################################################

lazy val `db-testkit-core` = subModule("db", "testkit-core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      logbackClassic,
      log4cats.withDottyCompat(scalaVersion.value),
      scalaTest.withDottyCompat(scalaVersion.value),
    )
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
    `db-core-flyway`,
    testkit,
  )
//#############################################################################

//used only in the interior of pureharm to test the implementations!
lazy val `db-test-data` = subModule("db", "test-data")
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
    `db-core-flyway`,
    `db-testkit-core`,
  )

//#############################################################################
lazy val `db-doobie` = subModule("db", "doobie")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      doobieCore.withDottyCompat(scalaVersion.value),
      doobieHikari.withDottyCompat(scalaVersion.value),
      doobiePSQL.withDottyCompat(scalaVersion.value),
      postgresql,
    )
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-psql`,
  )

//#############################################################################

lazy val `db-testkit-doobie` = subModule("db", "testkit-doobie")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core`,
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

lazy val `db-slick` = subModule("db", "slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      slick.withDottyCompat(scalaVersion.value),
      hikari,
    )
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-core-psql`,
  )

//#############################################################################

@scala.deprecated("Will be removed in the future, just depend on db-slick", "0.0.6-M3")
lazy val `db-slick-psql` = subModule("db", "slick-psql")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Nil
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-slick`,
    asTestingLibrary(`db-testkit-core`),
    asTestingLibrary(`db-testkit-slick`),
    asTestingLibrary(`db-test-data`),
  )

//#############################################################################

lazy val `db-testkit-slick` = subModule("db", "testkit-slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      slickTestkit.withDottyCompat(scalaVersion.value)
    )
  )
  .dependsOn(
    `core`,
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

lazy val `rest-http4s-tapir` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-rest-http4s-tapir",
    libraryDependencies ++= Seq(
      http4sDSL.withDottyCompat(scalaVersion.value),
      http4sCirce.withDottyCompat(scalaVersion.value),
      http4sServer.withDottyCompat(scalaVersion.value),
      tapirCore.withDottyCompat(scalaVersion.value),
      tapirCirce.withDottyCompat(scalaVersion.value),
      tapirHttp4s.withDottyCompat(scalaVersion.value),
      tapirOpenAPI.withDottyCompat(scalaVersion.value),
      tapirCirceYAML.withDottyCompat(scalaVersion.value),
    ),
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `json-circe`,
  )

//#############################################################################

lazy val `rest-http4s-tapir-testkit` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-rest-testkit-http4s-tapir",
    libraryDependencies ++= Nil,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `json-circe`,
    `rest-http4s-tapir`,
    testkit,
  )
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ TESTKIT ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val testkit = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-testkit",
    libraryDependencies ++= Seq(
      scalaTest.withDottyCompat(scalaVersion.value),
      log4cats.withDottyCompat(scalaVersion.value),
      logbackClassic,
    ),
  )
  .dependsOn(
    `core`,
    `effects-cats`,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val scalaCollCompatVersion: String = "2.1.6"     //https://github.com/scala/scala-collection-compat/releases
lazy val shapelessVersion:       String = "2.4.0-M1"  //https://github.com/milessabin/shapeless/releases
lazy val catsVersion:            String = "2.2.0-RC2" //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion:      String = "2.2.0-RC3" //https://github.com/typelevel/cats-effect/releases
lazy val fs2Version:             String = "2.4.2"     //https://github.com/functional-streams-for-scala/fs2/releases
lazy val circeVersion:           String = "0.13.0"    //https://github.com/circe/circe/releases
lazy val pureconfigVersion:      String = "0.13.0"    //https://github.com/pureconfig/pureconfig/releases
lazy val attoVersion:            String = "0.8.0"     //https://github.com/tpolecat/atto/releases
lazy val slickVersion:           String = "3.3.2"     //https://github.com/slick/slick/releases
lazy val postgresqlVersion:      String = "42.2.14"   //java — https://github.com/pgjdbc/pgjdbc/releases
lazy val hikariCPVersion:        String = "3.4.5"     //java — https://github.com/brettwooldridge/HikariCP/releases
lazy val doobieVersion:          String = "0.9.0"     //https://github.com/tpolecat/doobie/releases
lazy val flywayVersion:          String = "6.5.3"     //java — https://github.com/flyway/flyway/releases
lazy val log4catsVersion:        String = "1.1.1"     //https://github.com/ChristopherDavenport/log4cats/releases
lazy val logbackVersion:         String = "1.2.3"     //https://github.com/qos-ch/logback/releases
lazy val http4sVersion:          String = "0.21.6"    //https://github.com/http4s/http4s/releases
lazy val tapirVersion:           String = "0.16.10"   //https://github.com/softwaremill/tapir/releases
lazy val scalaTestVersion:       String = "3.2.0"     //https://github.com/scalatest/scalatest/releases

//=============================================================================
//=================================== SCALA ===================================
//=============================================================================

//https://github.com/scala/scala-collection-compat/releases
lazy val scalaCollectionCompat: ModuleID =
  "org.scala-lang.modules" %% "scala-collection-compat" % scalaCollCompatVersion withSources ()

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
lazy val shapeless: ModuleID = "com.chuusai" %% "shapeless" % shapelessVersion withSources ()

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
/**
  * See SBT docs:
  * https://www.scala-sbt.org/release/docs/Multi-Project.html#Per-configuration+classpath+dependencies
  *
  * Ensures dependencies between the ``test`` parts of the modules
  */
def fullDependency(p: Project): ClasspathDependency = p % "compile->compile;test->test"

/**
  * See SBT docs:
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

def subModule(parent: String, mod: String): Project =
  Project(id = s"pureharm-$parent-$mod", base = file(s"./$parent/submodules/$mod"))
    .settings(name := s"pureharm-$parent-$mod")
