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

lazy val mainVersion = "0.0.7-SNAPSHOT"
lazy val jsonVersion = "0.0.7-SNAPSHOT"
lazy val dbVersion   = "0.0.7-SNAPSHOT"
lazy val restVersion = "0.0.7-SNAPSHOT"

//see: https://github.com/liancheng/scalafix-organize-imports
//and the project-specific config in .scalafix.conf
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3"

// format: off
addCommandAlias("useScala213", s"++${CompilerSettings.scala2_13}")
addCommandAlias("useScala30",  s"++${CompilerSettings.scala3_0}")

addCommandAlias("recompile",      ";clean;compile;")
addCommandAlias("build",          ";compile;Test/compile")
addCommandAlias("rebuild",        ";clean;compile;Test/compile")
addCommandAlias("rebuild-update", ";clean;update;compile;Test/compile")
addCommandAlias("ci",             ";scalafmtCheck;rebuild-update;test")
addCommandAlias("ci-quick",       ";scalafmtCheck;build;test")
addCommandAlias("doLocal",        ";clean;update;compile;publishLocal")

addCommandAlias("cleanPublishSigned", ";recompile;publishSigned")
addCommandAlias("do213Release",       ";useScala213;cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("do30Release",        ";useScala30;cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("doRelease",          ";do213Release;do30Release")

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
    `rest-http4s-tapir`,
    `rest-http4s-tapir-testkit`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ CORE ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `core-anomaly` = mainModule("core-anomaly")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest % Test
    )
  )

//#############################################################################

lazy val `core-phantom` = mainModule("core-phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless,
      scalaTest % Test,
    )
  )

//#############################################################################

lazy val `core-identifiable` = mainModule("core-identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless,
      scalaTest % Test,
    )
  )
  .dependsOn(
    `core-phantom`
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ EFFECTS ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `effects-cats` = mainModule("effects-cats")
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

lazy val `testkit` = mainModule("testkit")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      log4cats,
      logbackClassic,
    ),
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ JSON +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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

lazy val `config` = mainModule("config")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless,
      pureConfig,
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

@scala.deprecated("Will be removed in the future, just depend on db-slick", "0.0.6-M3")
lazy val `db-slick-psql` = dbModule("slick-psql")
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
    `json-circe`,
    `db-core`,
    `db-slick`,
    asTestingLibrary(`db-testkit-core`),
    asTestingLibrary(`db-testkit-slick`),
    asTestingLibrary(`db-test-data`),
  )

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

lazy val shapelessVersion:  String = "2.4.0-M1"  //https://github.com/milessabin/shapeless/releases
lazy val catsVersion:       String = "2.2.0"     //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion: String = "2.2.0"     //https://github.com/typelevel/cats-effect/releases
lazy val fs2Version:        String = "2.4.4"     //https://github.com/functional-streams-for-scala/fs2/releases
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
lazy val http4sVersion:     String = "0.21.8"    //https://github.com/http4s/http4s/releases
lazy val tapirVersion:      String = "0.16.16"   //https://github.com/softwaremill/tapir/releases
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

def mainModule(mod: String): Project =
  Project(id = s"pureharm-$mod", base = file(s"main/$mod"))
    .settings(name := s"pureharm-$mod", version := mainVersion)

def jsonModule(mod: String): Project =
  Project(id = s"pureharm-json-$mod", base = file(s"json/$mod"))
    .settings(name := s"pureharm-json-$mod", version := jsonVersion)

def dbModule(mod:   String): Project =
  Project(id = s"pureharm-db-$mod", base = file(s"db/$mod"))
    .settings(name := s"pureharm-db-$mod", version := dbVersion)

def restModule(mod: String): Project =
  Project(id = s"pureharm-rest-$mod", base = file(s"rest/$mod"))
    .settings(name := s"pureharm-rest-$mod", version := restVersion)
