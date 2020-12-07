/** Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.config

import busymachines.pureharm.internals.config

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
trait PureconfigAllTypeDefinitions {
  final type ConfigReader[A] = pureconfig.ConfigReader[A]
  final val ConfigReader: pureconfig.ConfigReader.type = pureconfig.ConfigReader
  final type ConfigWriter[A] = pureconfig.ConfigWriter[A]
  final val ConfigWriter: pureconfig.ConfigWriter.type = pureconfig.ConfigWriter

  final type ConfigSource = pureconfig.ConfigSource
  final val ConfigSource: pureconfig.ConfigSource.type = pureconfig.ConfigSource

  final type ConfigLoader[A] = config.ConfigLoader[A]
  final val semiauto: pureconfig.generic.semiauto.type = pureconfig.generic.semiauto

  final type ConfigAggregateAnomalies = config.ConfigAggregateAnomalies
  final val ConfigAggregateAnomalies: config.ConfigAggregateAnomalies.type = config.ConfigAggregateAnomalies

  final type ConfigReadingAnomaly = config.ConfigReadingAnomaly
  final val ConfigReadingAnomaly: config.ConfigReadingAnomaly.type = config.ConfigReadingAnomaly

  final type ConfigSourceLoadingAnomaly = config.ConfigSourceLoadingAnomaly
  final val ConfigSourceLoadingAnomaly: config.ConfigSourceLoadingAnomaly.type = config.ConfigSourceLoadingAnomaly

  final type ConfigReaderFailure = pureconfig.error.ConfigReaderFailure
  final type ConvertFailure      = pureconfig.error.ConvertFailure
  final type ThrowableFailure    = pureconfig.error.ThrowableFailure
  final type CannotRead          = pureconfig.error.CannotRead
  final type CannotReadFile      = pureconfig.error.CannotReadFile
  final type CannotReadUrl       = pureconfig.error.CannotReadUrl
  final type CannotReadResource  = pureconfig.error.CannotReadResource
  final type CannotParse         = pureconfig.error.CannotParse

  final val ConvertFailure:     pureconfig.error.ConvertFailure.type     = pureconfig.error.ConvertFailure
  final val ThrowableFailure:   pureconfig.error.ThrowableFailure.type   = pureconfig.error.ThrowableFailure
  final val CannotReadFile:     pureconfig.error.CannotReadFile.type     = pureconfig.error.CannotReadFile
  final val CannotReadUrl:      pureconfig.error.CannotReadUrl.type      = pureconfig.error.CannotReadUrl
  final val CannotReadResource: pureconfig.error.CannotReadResource.type = pureconfig.error.CannotReadResource
  final val CannotParse:        pureconfig.error.CannotParse.type        = pureconfig.error.CannotParse

  final type ConfigReaderFailures     = pureconfig.error.ConfigReaderFailures
  final type ConfigReaderException[T] = pureconfig.error.ConfigReaderException[T]
}
