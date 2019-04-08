package busymachines.pureharm.dbslick.types

import busymachines.pureharm.dbslick.QueryAlgebraDefinitions

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
trait QueryAlgebraTypes { self: QueryAlgebraDefinitions =>
  type DBQueryAlgebra[E, PK, TA <: TableWithPK[E, PK]] = self.DBQueryAlgebra[E, PK, TA]

  type DBAlgebra[F[_], E, PK, TA <: TableWithPK[E, PK]] = self.DBAlgebra[F, E, PK, TA]

  type TableWithPK[E, PK] = self.TableWithPK[E, PK]
}
