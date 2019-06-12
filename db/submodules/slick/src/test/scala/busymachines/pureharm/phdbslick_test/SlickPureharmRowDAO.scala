package busymachines.pureharm.phdbslick_test

import busymachines.pureharm.db_test._
import busymachines.pureharm.phdbslick_test.db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */

private[phdbslick_test] object SlickPureharmRowDAO {

  def apply[F[_]: Transactor](implicit ec: ConnectionIOEC): PureharmRowDAO[F] =
    new PureharmRowDAOSlickImpl[F]

  //----------------- implementation details -----------------
  import db.implicits._

  private class SlickPureharmTable(tag: Tag) extends TableWithPK[PureharmRow, PhantomPK](tag, schema.PureharmRows) {
    val byte       = column[PhantomByte]("byte")
    val int        = column[PhantomInt]("int")
    val long       = column[PhantomLong]("long")
    val bigDecimal = column[PhantomBigDecimal]("big_decimal")
    val string     = column[PhantomString]("string")

    override def * : ProvenShape[PureharmRow] =
      (id, byte, int, long, bigDecimal, string) <> ((PureharmRow.apply _).tupled, PureharmRow.unapply)
  }

  final private class SlickPureharmRowQuerries(
    implicit override val connectionIOEC: ConnectionIOEC,
  ) extends SlickDAOQueryAlgebra[PureharmRow, PhantomPK, SlickPureharmTable] {
    override val dao: TableQuery[SlickPureharmTable] = TableQuery[SlickPureharmTable]
  }

  final private class PureharmRowDAOSlickImpl[F[_]](
    implicit override val connectionIOEC: ConnectionIOEC,
    implicit override val transactor:     Transactor[F],
  ) extends SlickDAOAlgebra[F, PureharmRow, PhantomPK, SlickPureharmTable] with PureharmRowDAO[F] {
    override protected val queries: SlickPureharmRowQuerries = new SlickPureharmRowQuerries
  }
}
