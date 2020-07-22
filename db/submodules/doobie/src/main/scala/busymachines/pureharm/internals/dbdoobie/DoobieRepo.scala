/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.db._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.implicits._
import busymachines.pureharm.effects._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
abstract class DoobieRepo[F[_], E, PK, TA <: TableWithPK[E, PK]](implicit
  val transactor: Transactor[F],
  val F:          BracketAttempt[F],
) extends Repo[F, E, PK] {

  final protected def transact[A](cio: ConnectionIO[A])(implicit transactor: Transactor[F]): F[A] =
    cio.transact(transactor)

  protected def queries: DoobieRepoQueries[E, PK, TA]

  override def find(pk: PK): F[Option[E]] = transact(queries.find(pk))

  override def retrieve(pk: PK)(implicit show: Show[PK]): F[E] = transact(queries.retrieve(pk))

  override def insert(e: E): F[PK] = transact(queries.insert(e))

  override def insertMany(es: Iterable[E]): F[Unit] = transact(queries.insertMany(es))

  override def update(e: E): F[E] = transact(queries.update(e))

  override def updateMany[M[_]: Traverse](es: M[E]): F[Unit] = transact(queries.updateMany(es))

  override def delete(pk: PK): F[Unit] = transact(queries.delete(pk))

  override def deleteMany(pks: Iterable[PK]): F[Unit] = transact(queries.deleteMany(pks))

  override def exists(pk: PK): F[Boolean] = transact(queries.exists(pk))

  override def existsAtLeastOne(pks: Iterable[PK]): F[Boolean] = transact(queries.existsAtLeastOne(pks))

  override def existAll(pks: Iterable[PK]): F[Boolean] = transact(queries.existAll(pks))
}
