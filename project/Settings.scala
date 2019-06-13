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
import sbt._
import Keys._

object Settings {
  lazy val scala2_12:        String = "2.12.8"
  lazy val scala2_13:        String = "2.13.0"
  lazy val mainScalaVersion: String = scala2_12
  lazy val organizationName: String = "com.busymachines"

  lazy val pureharmHomepage: String = "https://github.com/busymachines/pureharm"

  def commonSettings: Seq[Setting[_]] =
    Seq(
      organization in ThisBuild := organizationName,
      homepage                  := Some(url(pureharmHomepage)),
      scalaVersion              := mainScalaVersion,
      crossScalaVersions        := List(scala2_12, scala2_13),
      /*
       * Eliminates useless, unintuitive, and sometimes broken aditions of `withFilter`
       * when using generator arrows in for comprehensions. e.g.
       *
       * Vanila scala:
       * {{{
       *   for {
       *      x: Int <- readIntIO
       *      //
       *   } yield ()
       *   // instead of being `readIntIO.flatMap(x: Int => ...)`, it's something like .withFilter {case x: Int}, which is tantamount to
       *   // a runtime instanceof check. Absolutely horrible, and ridiculous, and unintuitive, and contrary to the often-
       *   // parroted mantra of "a for is just sugar for flatMap and map
       * }}}
       *
       * https://github.com/oleg-py/better-monadic-for
       */
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0"),
      //https://github.com/typelevel/kind-projector
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => scala2_12Flags
        case Some((2, 13)) => scala2_13Flags
        case _             => Seq.empty
      }) ++ betterForPluginCompilerFlags,
    )

  /**
    * tpolecat's glorious compile flag list:
    * https://tpolecat.github.io/2017/04/25/scalac-flags.html
    */
  def scala2_12Flags: Seq[String] = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfuture", // Turn on future language features.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match", // Pattern match may not be typesafe.
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals", // Warn if a local definition is unused.
    "-Ywarn-unused:params", // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates", // Warn if a private member is unused.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
    "-Ypartial-unification", // Enable partial unification in type constructor inference

    //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.

    //THESE are specific to typelevel scala 4 — check back once they get into core scala
    //"-Yinduction-heuristics",            // speeds up the compilation of inductive implicit resolution
    //"-Yliteral-types",                   // literals can appear in type position
    //"-Xstrict-patmat-analysis",          // more accurate reporting of failures of match exhaustivity
    //"-Xlint:strict-unsealed-patmat",     // warn on inexhaustive matches against unsealed traits
    //"-Ykind-polymorphism",               // type and method definitions with type parameters of arbitrary kinds
  )

  /**
    * tpolecat's glorious compile flag list adapted for scala 2.13 (fewer flags):
    * https://tpolecat.github.io/2017/04/25/scalac-flags.html
    */
  def scala2_13Flags: Seq[String] = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals", // Warn if a local definition is unused.
    "-Ywarn-unused:params", // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates", // Warn if a private member is unused.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.

    //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.

    //THESE are specific to typelevel scala 4 — check back once they get into core scala
    //"-Yinduction-heuristics",            // speeds up the compilation of inductive implicit resolution
    //"-Yliteral-types",                   // literals can appear in type position
    //"-Xstrict-patmat-analysis",          // more accurate reporting of failures of match exhaustivity
    //"-Xlint:strict-unsealed-patmat",     // warn on inexhaustive matches against unsealed traits
    //"-Ykind-polymorphism",               // type and method definitions with type parameters of arbitrary kinds
  )

  /**
    * These are flags specific to the "better-monadic-for" plugin:
    * https://github.com/oleg-py/better-monadic-for
    */
  def betterForPluginCompilerFlags: Seq[String] = Seq(
    "-P:bm4:no-filtering:y", // see https://github.com/oleg-py/better-monadic-for#desugaring-for-patterns-without-withfilters--pbm4no-filteringy
    "-P:bm4:no-map-id:y", // see https://github.com/oleg-py/better-monadic-for#final-map-optimization--pbm4no-map-idy
    "-P:bm4:no-tupling:y", // see https://github.com/oleg-py/better-monadic-for#desugar-bindings-as-vals-instead-of-tuples--pbm4no-tuplingy
    "-P:bm4:implicit-patterns:y", //see https://github.com/oleg-py/better-monadic-for#define-implicits-in-for-comprehensions-or-matches
  )
}
