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
//################################## README ##################################
//#############################################################################
//
// The reason all modules gather their dependencies up top, is so that
// downstream modules declare ALL their transitive dependencies explicitly
// because otherwise fetching source code for all is kinda bugged :(
// plus, this way it should be always clear that a module only puts its
// UNIQUE dependencies out in the clear. Everything else gets brought on
// transitively anyway. So whatever change you make, please respect the
// pattern that you see here. Maybe even borrow it for other projects.
//
//#############################################################################
//#############################################################################
//#############################################################################

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
addCommandAlias("do212Release",       s";useScala212;cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("do213Release",       s";useScala213;cleanPublishSigned;sonatypeBundleRelease")

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
    core,
    `effects-cats`,
    testkit,
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
    `core-phantom`
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
lazy val catsVersion:            String = "2.2.0-RC1" //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion:      String = "2.1.3"     //https://github.com/typelevel/cats-effect/releases
lazy val fs2Version:             String = "2.4.2"     //https://github.com/functional-streams-for-scala/fs2/releases
lazy val circeVersion:           String = "0.13.0"    //https://github.com/circe/circe/releases
lazy val pureconfigVersion:      String = "0.12.3"    //https://github.com/pureconfig/pureconfig/releases
lazy val attoVersion:            String = "0.8.0"     //https://github.com/tpolecat/atto/releases
lazy val slickVersion:           String = "3.3.2"     //https://github.com/slick/slick/releases
lazy val postgresqlVersion:      String = "42.2.14"   //java — https://github.com/pgjdbc/pgjdbc/releases
lazy val hikariCPVersion:        String = "3.4.5"     //java — https://github.com/brettwooldridge/HikariCP/releases
lazy val doobieVersion:          String = "0.9.0"     //https://github.com/tpolecat/doobie/releases
lazy val flywayVersion:          String = "6.4.4"     //java — https://github.com/flyway/flyway/releases
lazy val log4catsVersion:        String = "1.1.1"     //https://github.com/ChristopherDavenport/log4cats/releases
lazy val logbackVersion:         String = "1.2.3"     //https://github.com/qos-ch/logback/releases
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
