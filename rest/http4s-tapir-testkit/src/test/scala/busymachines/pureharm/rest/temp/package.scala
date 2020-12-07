/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.rest

import java.util.UUID

import busymachines.pureharm.phantom._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
package object temp {

  type PHString = PHString.Type

  object PHString extends PhantomType[String] {
    def unsafeGenerate: PHString = this.apply(scala.util.Random.nextString(10))
    def generate[F[_]: Sync]: F[PHString] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHLong = PHLong.Type

  object PHLong extends PhantomType[Long] {
    def unsafeGenerate: PHLong = this.apply((scala.util.Random.nextLong() % 9000L) + 1000L)
    def generate[F[_]: Sync]: F[PHLong] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHInt = PHInt.Type

  object PHInt extends PhantomType[Int] {
    def unsafeGenerate: PHInt = this.apply((scala.util.Random.nextInt() % 900) + 100)
    def generate[F[_]: Sync]: F[PHInt] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHUUID = PHUUID.Type

  object PHUUID extends PhantomType[UUID] {
    def unsafeGenerate: PHUUID = this.apply(UUID.randomUUID())
    def generate[F[_]: Sync]: F[PHUUID] = Sync[F].delay(unsafeGenerate)
  }

  type MyAuthToken = MyAuthToken.Type

  object MyAuthToken extends PhantomType[Long] {
    def unsafeGenerate: MyAuthToken = this.apply((scala.util.Random.nextLong() % 9000L) + 1000L)
    def generate[F[_]: Sync]: F[MyAuthToken] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHHeader = PHHeader.Type

  object PHHeader extends PhantomType[Long] {
    def unsafeGenerate: PHHeader = this.apply((scala.util.Random.nextLong() % 9000L) + 1000L)
    def generate[F[_]: Sync]: F[PHHeader] = Sync[F].delay(this.unsafeGenerate)
  }

  type SafePHUUIDStr = SafePHUUIDStr.Type

  object SafePHUUIDStr extends SafePhantomType[String, UUID] {
    override def check(value: UUID): Either[String, UUID] = value.pure[Either[String, *]]
    def unsafeGenerate: SafePHUUIDStr = this.unsafe(UUID.randomUUID())
    def generate[F[_]: Sync]: F[SafePHUUIDStr] = Sync[F].delay(unsafeGenerate)
  }

  type SafePHUUIDThr = SafePHUUIDThr.Type

  object SafePHUUIDThr extends SafePhantomType[Throwable, UUID] {
    override def check(value: UUID): Either[Throwable, UUID] = value.pure[Either[Throwable, *]]

    def unsafeGenerate: SafePHUUIDThr = this.unsafe(UUID.randomUUID())
    def generate[F[_]: Sync]: F[SafePHUUIDThr] = Sync[F].delay(unsafeGenerate)
  }
}
