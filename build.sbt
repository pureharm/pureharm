lazy val root = Project(id = "pureharm", base = file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .aggregate(
    core,
    `db-slick`,
  )

lazy val core = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-core",
    libraryDependencies ++= cats ++ Seq(
      shapeless,
      specs2 % Test,
    )
  )

lazy val `db-slick` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    name in ThisProject := "pureharm-db-slick",
    libraryDependencies ++= cats ++ dbSlick ++ Seq(
      catsEffect,
      specs2 % Test,
    )
  )
  .dependsOn(core)
  .aggregate(core)

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

//https://github.com/slick/slick
lazy val slick: ModuleID = "com.typesafe.slick" %% "slick" % slickVersion withSources ()

//https://github.com/brettwooldridge/HikariCP
lazy val hikari: ModuleID = "com.zaxxer" % "HikariCP" % hikariCPVersion withSources ()

//https://github.com/tminglei/slick-pg
//lazy val slickPG: ModuleID = "com.github.tminglei" %% "slick-pg" % slickPgVersion withSources ()

lazy val dbSlick: Seq[ModuleID] = Seq(slick, hikari)

//=============================================================================
//================================== TESTING ==================================
//=============================================================================

//https://github.com/etorreborre/specs2
lazy val specs2: ModuleID = "org.specs2" %% "specs2-core" % "4.3.6" withSources ()
