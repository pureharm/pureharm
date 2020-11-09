/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.anomaly

import busymachines.pureharm.anomaly

/** Mix this into your app's "core" package to get all these nice little
  * anomalies.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  */
trait PureharmAnomalyTypeDefinitions {

  final type AnomalyBase   = anomaly.AnomalyBase
  final type AnomaliesBase = anomaly.AnomaliesBase

  final type Anomaly = anomaly.Anomaly
  final val Anomaly: anomaly.Anomaly.type = anomaly.Anomaly

  final type Anomalies = anomaly.Anomalies
  final val Anomalies: anomaly.Anomalies.type = anomaly.Anomalies

  final type AnomalyID = anomaly.AnomalyID
  final val AnomalyID: anomaly.AnomalyID.type = anomaly.AnomalyID

  final type NotFoundAnomaly = anomaly.NotFoundAnomaly
  final val NotFoundAnomaly: anomaly.NotFoundAnomaly.type = anomaly.NotFoundAnomaly

  final type UnauthorizedAnomaly = anomaly.UnauthorizedAnomaly
  final val UnauthorizedAnomaly: anomaly.UnauthorizedAnomaly.type = anomaly.UnauthorizedAnomaly

  final type ForbiddenAnomaly = anomaly.ForbiddenAnomaly
  final val ForbiddenAnomaly: anomaly.ForbiddenAnomaly.type = anomaly.ForbiddenAnomaly

  final type DeniedAnomaly = anomaly.DeniedAnomaly
  final val DeniedAnomaly: anomaly.DeniedAnomaly.type = anomaly.DeniedAnomaly

  final type InvalidInputAnomaly = anomaly.InvalidInputAnomaly
  final val InvalidInputAnomaly: anomaly.InvalidInputAnomaly.type = anomaly.InvalidInputAnomaly

  final type ConflictAnomaly = anomaly.ConflictAnomaly
  final val ConflictAnomaly: anomaly.ConflictAnomaly.type = anomaly.ConflictAnomaly

  final type Catastrophe = anomaly.Catastrophe
  final val Catastrophe: anomaly.Catastrophe.type = anomaly.Catastrophe

  final type InconsistentStateCatastrophe = anomaly.InconsistentStateCatastrophe

  final val InconsistentStateCatastrophe: anomaly.InconsistentStateCatastrophe.type =
    anomaly.InconsistentStateCatastrophe

  final type NotImplementedCatastrophe = anomaly.NotImplementedCatastrophe
  final val NotImplementedCatastrophe: anomaly.NotImplementedCatastrophe.type = anomaly.NotImplementedCatastrophe

  final type UnhandledCatastrophe = anomaly.UnhandledCatastrophe
  final val UnhandledCatastrophe: anomaly.UnhandledCatastrophe.type = anomaly.UnhandledCatastrophe
}
