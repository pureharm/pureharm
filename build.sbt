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
addCommandAlias("doLocal", ";rebuild-update;publishLocal")

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
    `db-core`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++ CORE ++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val core = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-core",
  )
  .dependsOn(
    `core-phantom`,
    `core-identifiable`,
  )
  .aggregate(
    `core-phantom`,
    `core-identifiable`,
  )

lazy val `core-phantom` = subModule("core", "phantom")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-core-phantom",
    libraryDependencies ++= cats ++ Seq(
      shapeless,
      specs2 % Test,
    ),
  )

lazy val `core-identifiable` = subModule("core", "identifiable")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-core-identifiable",
    libraryDependencies ++= cats ++ Seq(
      shapeless,
      specs2 % Test,
    ),
  )
  .dependsOn(
    `core-phantom`,
  )
  .aggregate(
    `core-phantom`,
  )

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++ DB +++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

lazy val `db-core` = Project(id = "pureharm-db-core", base = file(s"./db"))
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-db-core",
    libraryDependencies ++= cats ++ dbSlick ++ Seq(
      catsEffect,
      specs2 % Test,
    ),
  )
  .dependsOn(
    `core-phantom`,
    `core-identifiable`,
  )
  .aggregate(
    `core-phantom`,
    `core-identifiable`,
  )

lazy val `db-slick` = subModule("db", "slick")
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-db-slick",
    libraryDependencies ++= Seq(
      specs2 % Test,
    ),
  )
  .dependsOn(
    `db-core`,
  )
  .aggregate(
    `db-core`,
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************

lazy val catsVersion:       String = "1.6.0"
lazy val catsEffectVersion: String = "1.2.0"

lazy val shapelessVersion: String = "2.3.3"

lazy val slickVersion:    String = "3.3.0"
lazy val slickPgVersion:  String = "0.17.2"
lazy val hikariCPVersion: String = "3.3.1"

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

//https://github.com/milessabin/shapeless
lazy val shapeless: ModuleID = "com.chuusai" %% "shapeless" % shapelessVersion withSources ()

//=============================================================================
//================================= DATABASE ==================================
//=============================================================================

//https://github.com/brettwooldridge/HikariCP
lazy val hikari: ModuleID = "com.zaxxer" % "HikariCP" % hikariCPVersion withSources ()

//=============================================================================
//============================= DATABASE - DOOBIE =============================
//=============================================================================

//=============================================================================
//============================= DATABASE - SLICK ==============================
//=============================================================================

//https://github.com/slick/slick
lazy val slick: ModuleID = "com.typesafe.slick" %% "slick" % slickVersion withSources ()

//https://github.com/tminglei/slick-pg
//lazy val slickPG: ModuleID = "com.github.tminglei" %% "slick-pg" % slickPgVersion withSources ()

lazy val dbSlick: Seq[ModuleID] = Seq(slick, hikari)

//=============================================================================
//================================== TESTING ==================================
//=============================================================================

//https://github.com/etorreborre/specs2
lazy val specs2: ModuleID = "org.specs2" %% "specs2-core" % "4.3.6" withSources ()

//=============================================================================
//================================== HELPERS ==================================
//=============================================================================

def subModule(parent: String, mod: String): Project =
  Project(id = s"pureharm-$parent-$mod", base = file(s"./$parent/submodules/$mod"))
