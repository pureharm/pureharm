/**
  * Copyright (c) 2017-2019 BusyMachines
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

import busymachines.{pureharm => pha}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  *
  */
trait PureharmAnomalyTypeDefinitions {

  final type AnomalyBase = pha.AnomalyBase

  final type Anomaly = pha.Anomaly
  final val Anomaly: pha.Anomaly.type = pha.Anomaly

  final type Anomalies = pha.Anomalies
  final val Anomalies: pha.Anomalies.type = pha.Anomalies

  final type AnomalyID = pha.AnomalyID
  final val AnomalyID: pha.AnomalyID.type = pha.AnomalyID

  final type NotFoundAnomaly = pha.NotFoundAnomaly
  final val NotFoundAnomaly: pha.NotFoundAnomaly.type = pha.NotFoundAnomaly

  final type UnauthorizedAnomaly = pha.UnauthorizedAnomaly
  final val UnauthorizedAnomaly: pha.UnauthorizedAnomaly.type = pha.UnauthorizedAnomaly

  final type ForbiddenAnomaly = pha.ForbiddenAnomaly
  final val ForbiddenAnomaly: pha.ForbiddenAnomaly.type = pha.ForbiddenAnomaly

  final type DeniedAnomaly = pha.DeniedAnomaly
  final val DeniedAnomaly: pha.DeniedAnomaly.type = pha.DeniedAnomaly

  final type InvalidInputAnomaly = pha.InvalidInputAnomaly
  final val InvalidInputAnomaly: pha.InvalidInputAnomaly.type = pha.InvalidInputAnomaly

  final type ConflictAnomaly = pha.ConflictAnomaly
  final val ConflictAnomaly: pha.ConflictAnomaly.type = pha.ConflictAnomaly

  final type Catastrophe = pha.Catastrophe
  final val Catastrophe: pha.Catastrophe.type = pha.Catastrophe

  final type InconsistentStateCatastrophe = pha.InconsistentStateCatastrophe
  final val InconsistentStateCatastrophe: pha.InconsistentStateCatastrophe.type = pha.InconsistentStateCatastrophe

  final type NotImplementedCatastrophe = pha.NotImplementedCatastrophe
  final val NotImplementedCatastrophe: pha.NotImplementedCatastrophe.type = pha.NotImplementedCatastrophe
}
