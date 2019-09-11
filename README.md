# pureharm
New, better, iteration of our `busymachines-commons` — short for "pure harmony"

Currently the project is under heavy development, and is mostly driven by company needs until a stable version can be put out. At the end of the day this is a principled utility library that provides all glue to make web server development a breeze. It encourages users to use the "pureharm" style, for each application creating, for instance, your own "myapp.effects" package, which is easily created by mixing in traits provided by pureharm + your own domain specific stuff. 

## sbt build
Project published on `bintray`, so add the following to your build if you don't want to wait until maven central syncs:
```scala
resolvers ++= Seq(
  Resolver.bintrayRepo("busymachines", "maven-releases"),
  Resolver.bintrayRepo("busymachines", "maven-snapshots"),
)
```

And then the available modules are:
```scala
val pureharmVersion: String = "0.0.2" //https://github.com/busymachines/pureharm/releases

def pureharm(m: String): ModuleID = "com.busymachines" %% s"pureharm-$m" % pureharmVersion

val pureharmCore:             ModuleID = pureharm("core")              withSources ()
val pureharmCoreAnomaly:      ModuleID = pureharm("core-anomaly")      withSources ()
val pureharmCorePhantom:      ModuleID = pureharm("core-phantom")      withSources ()
val pureharmCoreIdentifiable: ModuleID = pureharm("core-identifiable") withSources ()
val pureharmEffectsCats:      ModuleID = pureharm("effects-cats")      withSources ()
val pureharmJsonCirce:        ModuleID = pureharm("json-circe")        withSources ()
val pureharmConfig:           ModuleID = pureharm("config")            withSources ()

val pureharmDBCore:       ModuleID = pureharm("db-core")        withSources ()
val pureharmDBCoreFlyway: ModuleID = pureharm("db-core-flyway") withSources ()
val pureharmDBSlick:      ModuleID = pureharm("db-slick")       withSources ()
val pureharmDBPSQL:       ModuleID = pureharm("db-slick-psql")  withSources ()
//------- OR --------

libraryDependencies ++= Seq(
  //pureharm-core brings in pureharm-core-anomaly, pureharm-core-phantom, and pureharm-core-identifiable.
  "com.busymachines" %% s"pureharm-core"              % pureharmVersion,
  "com.busymachines" %% s"pureharm-core-anomaly"      % pureharmVersion,
  "com.busymachines" %% s"pureharm-core-phantom"      % pureharmVersion,
  "com.busymachines" %% s"pureharm-core-identifiable" % pureharmVersion,
  "com.busymachines" %% s"pureharm-effects-cats"      % pureharmVersion,
  "com.busymachines" %% s"pureharm-json-circe"        % pureharmVersion,
  "com.busymachines" %% s"pureharm-config"            % pureharmVersion,
  "com.busymachines" %% s"pureharm-db-core"           % pureharmVersion,
  "com.busymachines" %% s"pureharm-db-core-flyway"    % pureharmVersion,
  "com.busymachines" %% s"pureharm-db-slick"          % pureharmVersion,
  "com.busymachines" %% s"pureharm-db-slick-psql"     % pureharmVersion,
)
```

## Usage
Under construction. See [release notes](https://github.com/busymachines/pureharm/releases) and tests for examples.

## Copyright and License

All code is available to you under the Apache 2.0 license, available at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in the [LICENSE](./LICENSE) file.
