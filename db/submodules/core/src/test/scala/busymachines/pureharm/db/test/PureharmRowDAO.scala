package busymachines.pureharm.db.test

import busymachines.pureharm.db._

/**
  *
  * To be then implemented in the concrete slick, or doobie modules
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
private[pureharm] trait PureharmRowDAO[F[_]] extends DAOAlgebra[F, PureharmRow, PhantomPK]
