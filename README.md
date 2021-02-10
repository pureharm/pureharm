# pureharm

New, better, iteration of our `busymachines-commons` — short for "pure harmony"

Currently the project is under heavy development, and is mostly driven by company needs until a stable version can be
put out. At the end of the day this is a principled utility library that provides all glue to make web server
development a breeze. It encourages users to use the "pureharm" style, for each application creating, for instance, your
own "myapp.effects" package, which is easily created by mixing in traits provided by pureharm + your own domain specific
stuff.

## modules

The available modules are:

- kernel `0.0.7-M3` for Scala `2.13`
    - `"com.busymachines" %% s"pureharm-core" % "0.0.7-M3"`
        - [shapeless](https://github.com/milessabin/shapeless/releases) `2.4.0-M1`
    - `"com.busymachines" %% s"pureharm-effects-cats" % "0.0.7-M3"`
        - [cats](https://github.com/typelevel/cats/releases) `2.4.0`
        - [cats-effect](https://github.com/typelevel/cats-effect/releases) `2.3.1`
    - `"com.busymachines" %% s"pureharm-testkit" % "0.0.7-M3"`
        - [scalatest](https://github.com/scalatest/scalatest/releases) `3.2.3`
        - [log4cats](https://github.com/ChristopherDavenport/log4cats/releases) `1.2.0-RC2`
        - [logback-classic](https://github.com/qos-ch/logback/releases) `1.2.3`
- config `0.0.7-M3` for Scala `2.13`
    - `"com.busymachines" %% s"pureharm-config" % "0.0.7-M3"`
        - pureharm kernel `0.0.7-M3`
        - [pureconfig](https://github.com/pureconfig/pureconfig/releases) `0.14.0`
- json `0.0.7-M3` for Scala `2.13`
    - `"com.busymachines" %% s"pureharm-json" % "0.0.7-M3"`
        - pureharm kernel `0.0.7-M3`
        - [circe](https://github.com/circe/circe/releases) `0.13.0`
- db `0.0.7-M3` for Scala `2.13`
    - `"com.busymachines" %% s"pureharm-db-core" % "0.0.7-M3"`
        - pureharm kernel `0.0.7-M3`
        - pureharm config `0.0.7-M3`
    - `"com.busymachines" %% s"pureharm-db-core-flyway" % "0.0.7-M3"`
        - [flyway](https://github.com/flyway/flyway/releases) `7.5.3`
    - `"com.busymachines" %% s"pureharm-db-core-psql" % "0.0.7-M3"`
        - [postgresql](https://github.com/pgjdbc/pgjdbc/releases) `42.2.18`
        - [atto](https://github.com/tpolecat/atto/releases) `0.9.1`
    - `"com.busymachines" %% s"pureharm-db-doobie" % "0.0.7-M3"`
        - [doobie](https://github.com/tpolecat/doobie/releases) `0.11.0-M2`
    - `"com.busymachines" %% s"pureharm-db-slick" % "0.0.7-M3"`
        - [slick](https://github.com/slick/slick/releases) `3.3.3`
        - [HikariCP](https://github.com/brettwooldridge/HikariCP/releases) `3.4.5`
    - `"com.busymachines" %% s"pureharm-db-testkit-core" % "0.0.7-M3"`
    - `"com.busymachines" %% s"pureharm-db-testkit-doobie" % "0.0.7-M3"`
        - pureharm-db-testkit-core
    - `"com.busymachines" %% s"pureharm-db-testkit-slick" % "0.0.7-M3"`
        - pureharm-db-testkit-core
- rest `0.0.7-M3` for Scala `2.13`
    - `"com.busymachines" %% s"pureharm-rest-http4s-tapir" % "0.0.7-M3"`
        - pureharm-kernel `0.0.7-M3`
        - pureharm-json `0.0.7-M3`
        - [http4s](https://github.com/http4s/http4s/releases) `0.21.18`
        - [tapir](https://github.com/softwaremill/tapir/releases) `0.17.9`
    - `"com.busymachines" %% s"pureharm-rest-http4s-tapir-testkit" % "0.0.7-M3"`
        - pureharm kernel `0.0.7-M3`
        - pureharm-rest-http4s-tapir `0.0.7-M3`

## usage

Under construction. See [release notes](https://github.com/busymachines/pureharm/releases) and tests for examples.

## Copyright and License

All code is available to you under the Apache 2.0 license, available
at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in
the [LICENSE](./LICENSE) file.
