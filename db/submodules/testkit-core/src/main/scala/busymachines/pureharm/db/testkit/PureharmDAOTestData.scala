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

  def nonExistentPK: PK

  def row1: E
  def row2: E

  def row1Update1: E
  def row1Update2: E = row1Update1
  def row1Update3: E = row1Update1
  def row1Update4: E = row1Update1

  def pk1: PK = pk(row1)
  def pk2: PK = pk(row2)

  def isUniqueUpdate2: Boolean = isUniqueUpdate(row1Update2)
  def isUniqueUpdate3: Boolean = isUniqueUpdate(row1Update3)
  def isUniqueUpdate4: Boolean = isUniqueUpdate(row1Update4)

  private def pk(e: E): PK = iden.id(e)

  private def isUniqueUpdate(e: E): Boolean = updates.count(_ == e) == 1

  private lazy val updates = List(row1Update1, row1Update2, row1Update3, row1Update4)


}
