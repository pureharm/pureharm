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

addCommandAlias("build", ";compile;Test/compile")
addCommandAlias("rebuild", ";clean;compile;Test/compile")
addCommandAlias("rebuild-update", ";clean;update;compile;Test/compile")
addCommandAlias("ci", ";scalafmtCheck;rebuild-update;test")
addCommandAlias("ci-quick", ";scalafmtCheck;build;test")
addCommandAlias("doLocal", ";clean;update;compile;publishLocal")

/**
  * Use with care.
  *
  * All instructions for publishing to sonatype can be found in
  * ``z-publishing-artifcats/README.md``.
  */
addCommandAlias("doRelease", ";ci;publishSigned;sonatypeRelease")

//*****************************************************************************
//*****************************************************************************
//********************************* PROJECTS **********************************
//*****************************************************************************
//*****************************************************************************

lazy val root = Project(id = "pureharm", base = file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .aggregate(
    core,
    `effects-cats`,
    `json-circe`,
    `db`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ CORE ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val core = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "pureharm-core",
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

lazy val `core-anomaly` = subModule("core", "anomaly")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest % Test,
    ),
  )

lazy val `core-phantom` = subModule("core", "phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless,
      scalaTest % Test,
    ),
  )

lazy val `core-identifiable` = subModule("core", "identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      shapeless,
      scalaTest % Test,
    ),
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

lazy val `effects-cats` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "pureharm-effects-cats",
    libraryDependencies ++= cats ++ Seq(
      catsEffect,
      scalaCollectionCompat,
      scalaTest % Test,
    ),
  )
  .dependsOn(
    `core-phantom`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ JSON +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `json-circe` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "pureharm-json-circe",
    libraryDependencies ++= cats ++ circe ++ Seq(
      catsEffect,
      scalaCollectionCompat,
      scalaTest % Test,
    ),
  )
  .dependsOn(
    `core-anomaly`,
    `core-phantom`,
    `effects-cats`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ DB +++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `db` = project
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "pureharm-db",
  )
  .aggregate(
    `db-core`,
    `db-slick`,
  )

lazy val `db-core` = subModule("db", "core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      flyway,
      scalaTest      % Test,
      log4cats       % Test,
      logbackClassic % Test,
    ),
  )
  .dependsOn(
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
  )
  .aggregate(
    `core-phantom`,
    `core-identifiable`,
    `effects-cats`,
  )

lazy val `db-slick` = subModule("db", "slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= dbSlick ++ Seq(
      scalaTest      % Test,
      slickPG        % Test,
      log4cats       % Test,
      logbackClassic % Test,
    ),
  )
  .dependsOn(
    fullDependency(`db-core`),
    `effects-cats`,
  )
  .aggregate(
    `db-core`,
    `effects-cats`,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val scalaCollectionCompatVersion: String = "2.0.0"

lazy val catsVersion:       String = "2.0.0-M4"
lazy val catsEffectVersion: String = "2.0.0-M4"

lazy val circeVersion: String = "0.12.0-M3"

lazy val log4catsVersion: String = "0.4.0-M1"

lazy val shapelessVersion: String = "2.3.3"

lazy val slickVersion: String = "3.3.2"

lazy val hikariCPVersion: String = "3.3.1"
lazy val slickPGVersion:  String = "0.17.3" //used only to test db-slick

lazy val flywayVersion: String = "6.0.0-beta2"

lazy val scalaTestVersion: String = "3.1.0-SNAP13"

//=============================================================================
//=================================== SCALA ===================================
//=============================================================================

lazy val scalaCollectionCompat
  : ModuleID = "org.scala-lang.modules" %% "scala-collection-compat" % scalaCollectionCompatVersion

//=============================================================================
//================================= TYPELEVEL =================================
//=============================================================================

//https://github.com/typelevel/cats
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

//https://github.com/typelevel/cats-effect
lazy val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources ()

def circe: Seq[ModuleID] = Seq(circeCore, circeGenericExtras, circeParser)

lazy val circeCore:          ModuleID = "io.circe" %% "circe-core"           % circeVersion withSources ()
lazy val circeGenericExtras: ModuleID = "io.circe" %% "circe-generic-extras" % circeVersion withSources ()
lazy val circeParser:        ModuleID = "io.circe" %% "circe-parser"         % circeVersion withSources ()

//https://github.com/milessabin/shapeless
lazy val shapeless: ModuleID = "com.chuusai" %% "shapeless" % shapelessVersion withSources ()

//=============================================================================
//================================= DATABASE ==================================
//=============================================================================

//https://github.com/brettwooldridge/HikariCP
lazy val hikari: ModuleID = "com.zaxxer" % "HikariCP" % hikariCPVersion withSources ()

//https://github.com/flyway/flyway/releases
lazy val flyway: ModuleID = "org.flywaydb" % "flyway-core" % flywayVersion withSources ()

//https://github.com/tminglei/slick-pg — USED ONLY FOR TESTING WITH A REAL POSTGRESQL
lazy val slickPG: ModuleID = "com.github.tminglei" %% "slick-pg" % slickPGVersion withSources ()

//=============================================================================
//============================= DATABASE - DOOBIE =============================
//=============================================================================

//=============================================================================
//============================= DATABASE - SLICK ==============================
//=============================================================================

//https://github.com/slick/slick
lazy val slick: ModuleID = "com.typesafe.slick" %% "slick" % slickVersion withSources ()

lazy val dbSlick: Seq[ModuleID] = Seq(slick, hikari)

//=============================================================================
//================================== TESTING ==================================
//=============================================================================

//http://www.scalatest.org/
lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % scalaTestVersion withSources ()

//=============================================================================
//================================== HELPERS ==================================
//=============================================================================

//============================================================================================
//=========================================  logging =========================================
//============================================================================================
//https://github.com/ChristopherDavenport/log4cats
lazy val log4cats = ("io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion).withSources()

//it is the backend implementation used by log4cats
lazy val logbackClassic = ("ch.qos.logback" % "logback-classic" % "1.2.3").withSources()

//============================================================================================
//=======================================  build utils =======================================
//============================================================================================
/**
  * See SBT docs:
  * https://www.scala-sbt.org/release/docs/Multi-Project.html#Per-configuration+classpath+dependencies
  *
  * Ensures dependencies between the ``test`` parts of the modules
  */
def fullDependency(p: Project): ClasspathDependency = p % "compile->compile;test->test"

def subModule(parent: String, mod: String): Project =
  Project(id = s"pureharm-$parent-$mod", base = file(s"./$parent/submodules/$mod"))
    .settings(name := s"pureharm-$parent-$mod")
