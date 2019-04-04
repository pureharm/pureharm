package busymachines.pureharm.dbslick

import cats.Traverse

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait DAOAlgebra[R[_], E, PK] {
  def find(pk: PK): R[Option[E]]

  def retrieve(pk: PK): R[E]

  def insert(e: E): R[PK]

  def insertMany(es: Iterable[E]): R[Unit]

  def update(e: E): R[E]

  def updateMany[M[_]: Traverse](es: M[E]): R[Unit]

  def delete(pk: PK): R[Unit]

  def deleteMany(pks: Traversable[PK]): R[Unit]

  def exists(pk: PK): R[Boolean]

  def existsAtLeastOne(pks: Traversable[PK]): R[Boolean]

  def existAll(pks: Traversable[PK]): R[Boolean]
}
