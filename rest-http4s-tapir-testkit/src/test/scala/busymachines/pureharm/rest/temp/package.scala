package busymachines.pureharm.rest

import java.util.UUID

import busymachines.pureharm.phantom.PhantomType
import cats.effect.Sync

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
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
    def unsafeGenerate: PHLong = this.apply(scala.util.Random.between(1000L, 9000L))
    def generate[F[_]: Sync]: F[PHLong] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHInt = PHInt.Type

  object PHInt extends PhantomType[Int] {
    def unsafeGenerate: PHInt = this.apply(scala.util.Random.between(100: Int, 900: Int))
    def generate[F[_]: Sync]: F[PHInt] = Sync[F].delay(this.unsafeGenerate)
  }

  type PHUUID = PHUUID.Type

  object PHUUID extends PhantomType[UUID] {
    def unsafeGenerate: PHUUID = this.apply(UUID.randomUUID())
    def generate[F[_]: Sync]: F[PHUUID] = Sync[F].delay(unsafeGenerate)
  }

}
