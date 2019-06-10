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
package busymachines.pureharm.phdbslick.definitions

import busymachines.pureharm.phdbslick.SlickQueryAlgebraDefinitions

/**
  *
  * Copy paste these into your global JDBC profile API definition
  * to have them available whenever you actually write any slick
  * stuff. Thus easily making them available.
  *
  * Unfortunately, there is no way to define this in such a way as to
  * mix in directly into your "api" object, so you progrably will never use
  * this trait directly, but rather you will copy paste these definitions
  * into your custom application definition of ``slick.jdbc.JdbcProfile#api``,
  * for an easy import experience.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait SlickQueryAlgebraTypes { self: SlickQueryAlgebraDefinitions =>
  final type SlickDBQueryAlgebra[E, PK, TA <: TableWithPK[E, PK]] = self.SlickDBQueryAlgebra[E, PK, TA]

  final type SlickDBAlgebra[F[_], E, PK, TA <: TableWithPK[E, PK]] = self.SlickDBAlgebra[F, E, PK, TA]

  final type TableWithPK[E, PK] = self.TableWithPK[E, PK]
}
