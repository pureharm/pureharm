package busymachines.pureharm.dbslick.types

import busymachines.pureharm.dbslick.QueryAlgebraDefinitions

/**
  *
  * Copy paste these into your global JDBC profile API definition
  * to have them available whenever you actually write any slick
  * stuff. Thus easily making them available.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait QueryAlgebraTypes { self: QueryAlgebraDefinitions =>
  type DBQueryAlgebra[E, PK, TA <: TableWithPK[E, PK]] = self.DBQueryAlgebra[E, PK, TA]

  type DBAlgebra[F[_], E, PK, TA <: TableWithPK[E, PK]] = self.DBAlgebra[F, E, PK, TA]

  type TableWithPK[E, PK] = self.TableWithPK[E, PK]
}
