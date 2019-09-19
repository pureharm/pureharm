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
package busymachines.pureharm.internals.config

import busymachines.pureharm.config.ConfigAggregateAnomalies
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import pureconfig._
import pureconfig.error.ConfigReaderFailures

/**
  *
  * Important to note:
  * Given a case class:
  * {{{
  *   final case class TestConfig(
  *     port: Int,
  *     host: String,
  *     apiRoot: String,
  *   )
  * }}}
  * the ``apiRoot`` field will be read as ``api-root`` from the file.
  * So camelCase gets converted into "-" case.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 20 Jun 2018
  *
  */
trait ConfigLoader[Config] {

  /**
    * This exists to force semi-auto-derivation, and it allows us to build
    * proper functions. Simply do:
    *
    * {{{
    *   import busymachines.pureharm.config._
    *   import busymachines.pureharm.config.implicits._
    *
    *   final case class ConfigX(
    *   //stuff
    *   )
    *
    *   object ConfigX extends ConfigLoader[ConfigX]{
    *     override val configReader: ConfigReader[ConfigX] = semiauto.derive[ConfigX]
    *
    *     override def default[F: Sync] = this.load("test.config")
    *   }
    * }}}
    *
    * @return
    */
  implicit def configReader: ConfigReader[Config]

  def default[F[_]: Sync]: F[Config]

  /**
    * Override in subtypes when needed
    */
  def fromNamespace[F[_]: Sync](namespace: String): F[Config] = this.load(namespace)

  final def defaultR[F[_]: Sync]: Resource[F, Config] = Resource.liftF(default)

  final def fromNamespaceR[F[_]: Sync](namespace: String): Resource[F, Config] =
    Resource.liftF(fromNamespace(namespace))

  protected def load[F[_]: Sync](implicit reader: ConfigReader[Config]): F[Config] = {
    configToF(ConfigSource.default.load[Config](Derivation.Successful(reader)))
  }

  protected def load[F[_]: Sync](namespace: String)(implicit reader: ConfigReader[Config]): F[Config] = {
    configToF(ConfigSource.default.at(namespace).load[Config](Derivation.Successful(reader))).adaptError {
      case f: ConfigAggregateAnomalies => f.withNamespace(namespace)
    }
  }

  private def configToF[F[_]](thunk: => Either[ConfigReaderFailures, Config])(implicit F: Sync[F]): F[Config] = {
    F.delay(thunk.leftMap((err: ConfigReaderFailures) => ConfigAggregateAnomalies(err): Throwable)).rethrow
  }
}
