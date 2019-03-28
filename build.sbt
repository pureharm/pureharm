lazy val root = Project(id = "pureharm", base = file("."))
  .settings(PublishingSettings.noPublishSettings)
  .settings(Settings.commonSettings)
  .aggregate(
    `db-slick`,
  )

lazy val `db-slick` = project
  .settings(PublishingSettings.sonatypeSettings)
  .settings(Settings.commonSettings)
  .settings(
    libraryDependencies ++= cats ++ dbSlick ++ Seq(
      catsEffect
    )
  )

//*****************************************************************************
//*****************************************************************************
//******************************** DEPENDENCIES *******************************
//*****************************************************************************
//*****************************************************************************
lazy val catsVersion:       String = "1.6.0"
lazy val catsEffectVersion: String = "1.2.0"

lazy val slickVersion: String = "3.3.0"

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

//=============================================================================
//================================= DATABASE ==================================
//=============================================================================

lazy val slick: ModuleID = "com.typesafe.slick" %% "slick" % slickVersion withSources ()

lazy val dbSlick: Seq[ModuleID] = Seq(slick)
