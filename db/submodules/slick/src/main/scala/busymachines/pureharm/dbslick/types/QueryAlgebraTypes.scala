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
package busymachines.pureharm.dbslick.types

import busymachines.pureharm.dbslick.SlickQueryAlgebraDefinitions

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
trait QueryAlgebraTypes { self: SlickQueryAlgebraDefinitions =>
  type DBQueryAlgebra[E, PK, TA <: TableWithPK[E, PK]] = self.SlickDBQueryAlgebra[E, PK, TA]

  type DBAlgebra[F[_], E, PK, TA <: TableWithPK[E, PK]] = self.SlickDBAlgebra[F, E, PK, TA]

  type TableWithPK[E, PK] = self.TableWithPK[E, PK]
}
