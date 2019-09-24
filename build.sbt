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
addCommandAlias("recompile",      ";clean;update;compile")
addCommandAlias("build",          ";compile;Test/compile")
addCommandAlias("rebuild",        ";clean;compile;Test/compile")
addCommandAlias("rebuild-update", ";clean;update;compile;Test/compile")
addCommandAlias("ci",             ";scalafmtCheck;rebuild-update;test")
addCommandAlias("ci-quick",       ";scalafmtCheck;build;test")
addCommandAlias("doLocal",        ";clean;update;compile;publishLocal")

addCommandAlias("cleanPublishSigned", ";recompile;publishSigned")
addCommandAlias("do212Release",       s";++${CompilerSettings.scala2_12};cleanPublishSigned;sonatypeBundleRelease")
addCommandAlias("do213Release",       s";++${CompilerSettings.scala2_13};cleanPublishSigned;sonatypeBundleRelease")
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
    core,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ CORE ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `core-deps` = `core-anomaly-deps` ++ `core-phantom-deps` ++ `core-identifiable-deps`

lazy val core = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-core",
    libraryDependencies ++= `core-deps`.distinct,
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
  )
  .aggregate(
    `core-anomaly`,
    `core-phantom`,
    `core-identifiable`,
  )

//#############################################################################

lazy val `core-anomaly-deps` = Seq(
  scalaTest % Test,
)

lazy val `core-anomaly` = subModule("core", "anomaly")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `core-anomaly-deps`.distinct,
  )

//#############################################################################

lazy val `core-phantom-deps` = Seq(
  shapeless,
  scalaTest % Test,
)

lazy val `core-phantom` = subModule("core", "phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `core-phantom-deps`.distinct,
  )

//#############################################################################

lazy val `core-identifiable-deps` = `core-phantom-deps` ++ Seq(
  shapeless,
  scalaTest % Test,
)

lazy val `core-identifiable` = subModule("core", "identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `core-identifiable-deps`.distinct,
  )
  .dependsOn(
    `core-phantom`,
  )
  .aggregate(
    `core-phantom`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ EFFECTS ++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `effects-cats-deps` = `core-phantom-deps` ++ Seq(
  shapeless,
  catsEffect,
  scalaCollectionCompat,
  scalaTest % Test,
) ++ cats

lazy val `effects-cats` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-effects-cats",
    libraryDependencies ++= `effects-cats-deps`.distinct,
  )
  .dependsOn(
    `core-phantom`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ JSON +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `json-circe-deps` = `effects-cats-deps` ++ Seq(
  shapeless,
  scalaTest % Test,
) ++ circe

lazy val `json-circe` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-json-circe",
    libraryDependencies ++= `json-circe-deps`.distinct,
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++ CONFIG +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `config-deps` = `core-anomaly-deps` ++ `core-phantom-deps` ++ `effects-cats-deps` ++ Seq(
  shapeless,
  pureConfig,
  scalaTest % Test,
)

lazy val `config` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-config",
    libraryDependencies ++= `config-deps`.distinct,
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ DB +++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `db-deps` =
  `db-core-deps` ++
    `db-core-flyway-deps` ++
    `db-slick-deps` ++
    `db-slick-psql-deps`

lazy val `db` = project
  .settings(PublishingSettings.noPublishSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    name := "pureharm-db",
    libraryDependencies ++= `db-deps`.distinct,
  )
  .aggregate(
    `db-core`,
    `db-core-flyway`,
    `db-slick`,
    `db-slick-psql`,
  )

//#############################################################################

lazy val `db-core-deps` = `core-deps` ++ `effects-cats-deps` ++ `config-deps` ++ Seq(
  flyway, //FIXME: remove in M17 once the deprecation period of Flyway is gone
  log4cats       % Test,
  logbackClassic % Test,
  scalaTest      % Test,
)

lazy val `db-core` = subModule("db", "core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `db-core-deps`.distinct,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
  )
  .aggregate(
    `core`,
    `effects-cats`,
    `config`,
  )

//#############################################################################

lazy val `db-core-flyway-deps` = `core-deps` ++ `effects-cats-deps` ++ `config-deps` ++ `db-core-deps` ++ Seq(
  flyway,
  log4cats       % Test,
  logbackClassic % Test,
  scalaTest      % Test,
)

lazy val `db-core-flyway` = subModule("db", "core-flyway")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `db-core-flyway-deps`.distinct,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    fullDependency(`db-core`),
  )
  .aggregate(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
  )

//#############################################################################

lazy val `db-doobie-deps` =
  `core-deps` ++
    `effects-cats-deps` ++
    `config-deps` ++
    `json-circe-deps` ++
    `db-core-deps` ++ Seq(
    doobieCore,
    doobiePSQL,
    postgresql,
    log4cats        % Test,
    logbackClassic  % Test,
    scalaTest       % Test,
    doobieScalatest % Test,
  )

lazy val `db-doobie` = subModule("db", "db-doobie")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `db-doobie-deps`.distinct,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    fullDependency(`db-core`),
    asTestingDependency(`db-core-flyway`),
  )
  .aggregate(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
  )

//#############################################################################

lazy val `db-slick-deps` = `core-deps` ++ `effects-cats-deps` ++ `config-deps` ++ `db-core-deps` ++ Seq(
  scalaTest % Test,
) ++ dbSlick

lazy val `db-slick` = subModule("db", "slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `db-slick-deps`.distinct,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    fullDependency(`db-core`),
  )
  .aggregate(
    `core`,
    `effects-cats`,
    `config`,
    `db-core`,
  )

//#############################################################################

lazy val `db-slick-psql-deps` =
  `core-deps` ++
    `effects-cats-deps` ++
    `config-deps` ++
    `json-circe-deps` ++
    `db-core-deps` ++
    `db-slick-deps` ++ Seq(
    postgresql,
    log4cats       % Test,
    logbackClassic % Test,
    scalaTest      % Test,
  )

lazy val `db-slick-psql` = subModule("db", "slick-psql")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(CompilerSettings.commonSettings)
  .settings(
    libraryDependencies ++= `db-slick-psql-deps`.distinct,
  )
  .dependsOn(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    fullDependency(`db-core`),
    asTestingDependency(`db-core-flyway`),
    `db-slick`,
  )
  .aggregate(
    `core`,
    `effects-cats`,
    `config`,
    `json-circe`,
    `db-core`,
    `db-slick`,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val scalaCollCompatVersion: String = "2.1.2"        //https://github.com/scala/scala-collection-compat/releases
lazy val shapelessVersion:       String = "2.3.3"        //https://github.com/milessabin/shapeless/releases
lazy val catsVersion:            String = "2.0.0"        //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion:      String = "2.0.0"        //https://github.com/typelevel/cats-effect/releases
lazy val fs2Version:             String = "2.0.1"        //https://github.com/functional-streams-for-scala/fs2/releases
lazy val circeVersion:           String = "0.12.1"       //https://github.com/circe/circe/releases
lazy val pureconfigVersion:      String = "0.12.0"       //https://github.com/pureconfig/pureconfig/releases
lazy val slickVersion:           String = "3.3.2"        //https://github.com/slick/slick/releases
lazy val postgresqlVersion:      String = "42.2.8"       //java — https://github.com/pgjdbc/pgjdbc/releases
lazy val hikariCPVersion:        String = "3.4.1"        //java — https://github.com/brettwooldridge/HikariCP/releases
lazy val doobieVersion:          String = "0.8.2"        //https://github.com/tpolecat/doobie/releases
lazy val flywayVersion:          String = "6.0.3"        //java — https://github.com/flyway/flyway/releases
lazy val log4catsVersion:        String = "1.0.0"        //https://github.com/ChristopherDavenport/log4cats/releases
lazy val logbackVersion:         String = "1.2.3"        //https://github.com/qos-ch/logback/releases
lazy val scalaTestVersion:       String = "3.1.0-SNAP13" //https://github.com/scalatest/scalatest/releases

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
lazy val catsCore:    ModuleID = "org.typelevel" %% "cats-core"    % catsVersion withSources ()
lazy val catsMacros:  ModuleID = "org.typelevel" %% "cats-macros"  % catsVersion withSources ()
lazy val catsKernel:  ModuleID = "org.typelevel" %% "cats-kernel"  % catsVersion withSources ()
lazy val catsLaws:    ModuleID = "org.typelevel" %% "cats-laws"    % catsVersion withSources ()
lazy val catsTestkit: ModuleID = "org.typelevel" %% "cats-testkit" % catsVersion withSources ()

lazy val cats: Seq[ModuleID] = Seq(
  catsCore,
  catsMacros,
  catsKernel,
  catsLaws,
  catsTestkit % Test,
)

//https://github.com/typelevel/cats-effect/releases
lazy val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources ()

//https://github.com/circe/circe/releases
def circe: Seq[ModuleID] = Seq(circeCore, circeGenericExtras, circeParser)

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
lazy val doobieCore      = "org.tpolecat" %% "doobie-core"      % doobieVersion withSources ()
lazy val doobiePSQL      = "org.tpolecat" %% "doobie-postgres"  % doobieVersion withSources ()
lazy val doobieScalatest = "org.tpolecat" %% "doobie-scalatest" % doobieVersion withSources ()

//=============================================================================
//============================= DATABASE - SLICK ==============================
//=============================================================================

//https://github.com/slick/slick/releases
lazy val slick: ModuleID = "com.typesafe.slick" %% "slick" % slickVersion withSources ()

lazy val dbSlick: Seq[ModuleID] = Seq(slick, hikari)

//=============================================================================
//================================== TESTING ==================================
//=============================================================================

//https://github.com/scalatest/scalatest/releases
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestVersion withSources ()

//=============================================================================
//================================== HELPERS ==================================
//=============================================================================

lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % pureconfigVersion withSources ()

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
  * Used only when one module is useful to test another module, but
  * in production build they don't require to be used together.
  */
def asTestingDependency(p: Project): ClasspathDependency = p % "test -> compile"

def subModule(parent: String, mod: String): Project =
  Project(id = s"pureharm-$parent-$mod", base = file(s"./$parent/submodules/$mod"))
    .settings(name := s"pureharm-$parent-$mod")
