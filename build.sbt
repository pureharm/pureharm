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

addCommandAlias("build", ";compile;Test/compile")
addCommandAlias("rebuild", ";clean;compile;Test/compile")
addCommandAlias("rebuild-update", ";clean;update;compile;Test/compile")
addCommandAlias("ci", ";scalafmtCheck;rebuild-update;test")
addCommandAlias("ci-quick", ";scalafmtCheck;build;test")
addCommandAlias("doLocal", ";clean;update;compile;publishLocal")
addCommandAlias("doRelease", ";ci;publishSigned;sonatypeRelease")

addCommandAlias("lint", ";scalafixEnable;rebuild;scalafix;scalafmtAll")

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
    `config`,
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
      shapeless,
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
      shapeless,
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
//++++++++++++++++++++++++++++++++++ CONFIG +++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `config` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name := "pureharm-config",
    libraryDependencies ++= cats ++ Seq(
      shapeless,
      catsEffect,
      scalaCollectionCompat,
      scalaTest % Test,
      pureConfig,
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
    `db-slick-psql`,
  )

lazy val `db-core` = subModule("db", "core")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= cats ++ Seq(
      shapeless,
      catsEffect,
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
    libraryDependencies ++= cats ++ dbSlick ++ Seq(
      shapeless,
      catsEffect,
      flyway,
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

lazy val `db-slick-psql` = subModule("db", "slick-psql")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= cats ++ dbSlick ++ Seq(
      shapeless,
      catsEffect,
      flyway,
      postgresql,
      scalaTest      % Test,
      log4cats       % Test,
      logbackClassic % Test,
    ),
  )
  .dependsOn(
    fullDependency(`db-core`),
    `db-slick`,
    `effects-cats`,
    `json-circe`,
  )
  .aggregate(
    `db-core`,
    `db-slick`,
    `effects-cats`,
    `json-circe`,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val scalaCollCompatVersion: String = "2.0.0"        //https://github.com/scala/scala-collection-compat/releases
lazy val shapelessVersion:       String = "2.3.3"        //https://github.com/milessabin/shapeless/releases
lazy val catsVersion:            String = "2.0.0-M4"     //https://github.com/typelevel/cats/releases
lazy val catsEffectVersion:      String = "2.0.0-M4"     //https://github.com/typelevel/cats-effect/releases
lazy val circeVersion:           String = "0.12.0-M3"    //https://github.com/circe/circe/releases
lazy val log4catsVersion:        String = "0.4.0-M1"     //https://github.com/ChristopherDavenport/log4cats/releases
lazy val logbackVersion:         String = "1.2.3"        //https://github.com/qos-ch/logback/releases
lazy val pureconfigVersion:      String = "0.11.1"       //https://github.com/pureconfig/pureconfig/releases
lazy val slickVersion:           String = "3.3.2"        //https://github.com/slick/slick/releases
lazy val postgresqlVersion:      String = "42.2.5"       //https://github.com/pgjdbc/pgjdbc/releases
lazy val hikariCPVersion:        String = "3.3.1"        //java — https://github.com/brettwooldridge/HikariCP/releases
lazy val flywayVersion:          String = "6.0.0-beta2"  //java — https://github.com/flyway/flyway/releases
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

def circe: Seq[ModuleID] = Seq(circeCore, circeGenericExtras, circeParser)

lazy val circeCore:          ModuleID = "io.circe" %% "circe-core"           % circeVersion withSources ()
lazy val circeGenericExtras: ModuleID = "io.circe" %% "circe-generic-extras" % circeVersion withSources ()
lazy val circeParser:        ModuleID = "io.circe" %% "circe-parser"         % circeVersion withSources ()

//https://github.com/milessabin/shapeless/releases
lazy val shapeless: ModuleID = "com.chuusai" %% "shapeless" % shapelessVersion withSources ()

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

def subModule(parent: String, mod: String): Project =
  Project(id = s"pureharm-$parent-$mod", base = file(s"./$parent/submodules/$mod"))
    .settings(name := s"pureharm-$parent-$mod")
