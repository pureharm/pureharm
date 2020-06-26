package busymachines.pureharm.db.testkit

import busymachines.pureharm.identifiable.Identifiable

/**
  * Basic representation of data that the user ought to provide
  * for a full test of the [[busymachines.pureharm.db.DAOAlgebra]]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
trait PureharmDAOTestData[E, PK] {

  def iden: Identifiable[E, PK]

  def row1: E
  def row2: E

  def rows: List[E] = List(row1, row2)

  def pk(e: E): PK = iden.id(e)

  def randomPK: PK

}
