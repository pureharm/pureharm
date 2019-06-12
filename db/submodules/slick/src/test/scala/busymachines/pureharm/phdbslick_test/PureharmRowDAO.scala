package busymachines.pureharm.phdbslick_test

import db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
sealed private[phdbslick_test] trait PureharmRowDAO[F[_]] extends DAOAlgebra[F, PureharmRow, PhantomPK]

private[phdbslick_test] object PureharmRowDAO {

  def apply[F[_]: Transactor](implicit ec: ConnectionIOEC): PureharmRowDAO[F] =
    new PureharmRowDAOImpl[F]

  //----------------- implementation details -----------------
  import db.implicits._

  private class PureharmTable(tag: Tag) extends TableWithPK[PureharmRow, PhantomPK](tag, schema.PureharmRows) {
    val byte       = column[PhantomByte]("byte")
    val int        = column[PhantomInt]("int")
    val long       = column[PhantomLong]("long")
    val bigDecimal = column[PhantomBigDecimal]("big_decimal")
    val string     = column[PhantomString]("string")

    override def * : ProvenShape[PureharmRow] =
      (id, byte, int, long, bigDecimal, string) <> ((PureharmRow.apply _).tupled, PureharmRow.unapply)
  }

  final private class PureharmRowQuerries(
    implicit override val connectionIOEC: ConnectionIOEC,
  ) extends SlickDAOQueryAlgebra[PureharmRow, PhantomPK, PureharmTable] {
    override val dao: TableQuery[PureharmTable] = TableQuery[PureharmTable]
  }

  final private class PureharmRowDAOImpl[F[_]](
    implicit override val connectionIOEC: ConnectionIOEC,
    implicit override val transactor:     Transactor[F],
  ) extends SlickDAOAlgebra[F, PureharmRow, PhantomPK, PureharmTable] with PureharmRowDAO[F] {
    override protected val queries: PureharmRowQuerries = new PureharmRowQuerries
  }
}
