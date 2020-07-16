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
import dotty.tools.sbtplugin.DottyPlugin.autoImport._

object CompilerSettings {
  lazy val scala2_12:        String = "2.12.11"    //https://github.com/scala/scala/releases
  lazy val scala2_13:        String = "2.13.3"     //https://github.com/scala/scala/releases
  lazy val dottyVersion:     String = "0.25.0-RC2" //https://github.com/lampepfl/dotty/releases
  lazy val mainScalaVersion: String = scala2_13

  //https://github.com/typelevel/kind-projector/releases
  lazy val kindProjector    = "org.typelevel" %% "kind-projector"     % "0.11.0"
  //https://github.com/oleg-py/better-monadic-for/releases
  lazy val betterMonadicFor = "com.olegpy"    %% "better-monadic-for" % "0.3.1"

  //https://github.com/tek/splain
  lazy val splain = "io.tryp" % "splain" % "0.5.7"

  lazy val organizationName: String = "com.busymachines"
  lazy val pureharmHomepage: String = "https://github.com/busymachines/pureharm"

  def commonSettings: Seq[Setting[_]] =
    Seq(
      organization in ThisBuild := organizationName,
      homepage                  := Some(url(pureharmHomepage)),
      scalaVersion              := mainScalaVersion,
      crossScalaVersions        := List(scala2_12, scala2_13, dottyVersion),
      libraryDependencies ++= (if (isDotty.value) {
                                 Nil
                               }
                               else {
                                 Seq(
                                   compilerPlugin(kindProjector.cross(CrossVersion.full)),
                                   compilerPlugin(betterMonadicFor),
                                   //compilerPlugin(splain).cross(CrossVersion.patch),
                                 )
                               }),
      scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => scala2_12Flags ++ betterForPluginCompilerFlags
        case Some((2, 13)) => scala2_13Flags ++ betterForPluginCompilerFlags
        case _             => dottyFlags
      }),
      javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    )

  /**
    * tpolecat's glorious compile flag list:
    * https://tpolecat.github.io/2017/04/25/scalac-flags.html
    */
  def scala2_12Flags: Seq[String] = Seq(
    //"-Xfatal-warnings",               // Fail the compilation if there are any warnings.
    "-deprecation",                     // Emit warning and location for usages of deprecated APIs.
    "-encoding",                        // yeah, it's part of the "utf-8" thing, two flags
    "utf-8",                            // Specify character encoding used by source files.
    "-explaintypes",                    // Explain type errors in more detail.
    "-feature",                         // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials",           // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds",            // Allow higher-kinded types
    "-language:implicitConversions",    // Allow definition of implicit functions called views
    "-unchecked",                       // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit",                      // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfuture",                         // Turn on future language features.
    "-Xlint:adapted-args",              // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant",                  // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select",        // Selecting member of DelayedInit.
    "-Xlint:doc-detached",              // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible",              // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",                 // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator",      // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override",          // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit",              // Warn when nullary methods return Unit.
    "-Xlint:option-implicit",           // Option.apply used implicit view.
    "-Xlint:package-object-classes",    // Class or object defined in package object.
    "-Xlint:poly-implicit-overload",    // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow",            // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align",               // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow",     // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match",             // Pattern match may not be typesafe.
    "-Yno-adapted-args",                // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ywarn-extra-implicit",            // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible",              // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any",                 // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override",          // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit",              // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen",             // Warn when numerics are widened.
    "-Ywarn-unused:implicits",          // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports",            // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals",             // Warn if a local definition is unused.
    "-Ywarn-unused:params",             // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars",            // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates",           // Warn if a private member is unused.
    "-Ywarn-value-discard",             // Warn when non-Unit expression results are unused.
    "-Ypartial-unification",            // Enable partial unification in type constructor inference
  )

  /**
    * tpolecat's glorious compile flag list adapted for scala 2.13 (fewer flags):
    * https://tpolecat.github.io/2017/04/25/scalac-flags.html
    */
  def scala2_13Flags: Seq[String] = Seq(
    //"-Xfatal-warnings",            // Fail the compilation if there are any warnings.
    "-deprecation",                  // Emit warning and location for usages of deprecated APIs.
    "-encoding",                     // yeah, it's part of the "utf-8" thing, two flags
    "utf-8",                         // Specify character encoding used by source files.
    "-explaintypes",                 // Explain type errors in more detail.
    "-feature",                      // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds",         // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked",                    // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit",                   // Wrap field accessors to throw an exception on uninitialized access.
    "-Xlint:adapted-args",           // Warn if an argument list is modified to match the receiver.
    "-Xlint:constant",               // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select",     // Selecting member of DelayedInit.
    "-Xlint:doc-detached",           // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible",           // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",              // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator",   // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-unit",           // Warn when nullary methods return Unit.
    "-Xlint:option-implicit",        // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow",         // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align",            // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow",  // A local type parameter shadows a type already in scope.
    "-Wdead-code",                   // Warn when we have dead code
    "-Ywarn-extra-implicit",         // Warn when more than one implicit parameter section is defined.
    "-Ywarn-numeric-widen",          // Warn when numerics are widened.
    "-Ywarn-unused:implicits",       // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports",         // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals",          // Warn if a local definition is unused.
    "-Ywarn-unused:params",          // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars",         // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates",        // Warn if a private member is unused.
    "-Ywarn-value-discard",          // Warn when non-Unit expression results are unused.
    "-Wconf:any:warning-verbose",    // Gives extra information about warning
  )

  def dottyFlags: Seq[String] = Seq(
    "-language:Scala2Compat",
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-deprecation",                  // Emit warning and location for usages of deprecated APIs.
    "-encoding",                     // yeah, it's part of the "utf-8" thing, two flags
    "utf-8",                         // Specify character encoding used by source files.
    "-feature",                      // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked",                    // Enable additional warnings where generated code depends on assumptions.
  )

  /**
    * These are flags specific to the "better-monadic-for" plugin:
    * https://github.com/oleg-py/better-monadic-for
    */
  def betterForPluginCompilerFlags: Seq[String] = Seq(
    "-P:bm4:no-filtering:y",     // see https://github.com/oleg-py/better-monadic-for#desugaring-for-patterns-without-withfilters--pbm4no-filteringy
    "-P:bm4:no-map-id:y",        // see https://github.com/oleg-py/better-monadic-for#final-map-optimization--pbm4no-map-idy
    "-P:bm4:no-tupling:y",       // see https://github.com/oleg-py/better-monadic-for#desugar-bindings-as-vals-instead-of-tuples--pbm4no-tuplingy
    "-P:bm4:implicit-patterns:y",// see https://github.com/oleg-py/better-monadic-for#define-implicits-in-for-comprehensions-or-matches
  )
}
