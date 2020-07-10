package busymachines.pureharm.rest

import java.util.UUID

import busymachines.pureharm.phantom.PhantomType

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
package object temp {
  object PHString extends PhantomType[String]
  type PHString = PHString.Type

  object PHLong extends PhantomType[Long]
  type PHLong = PHLong.Type

  object PHInt extends PhantomType[Int]
  type PHInt = PHInt.Type

  object PHUUID extends PhantomType[UUID]
  type PHUUID = PHUUID.Type
}
