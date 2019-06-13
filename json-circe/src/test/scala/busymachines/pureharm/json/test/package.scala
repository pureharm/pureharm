package busymachines.pureharm.json

import busymachines.pureharm.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
package object test {

  object Weight extends PhantomType[Int]
  type Weight = Weight.Type

  object Weights extends PhantomType[List[Int]]
  type Weights = Weights.Type

  object WeigthsSet extends PhantomType[Set[Int]]
  type WeigthsSet = WeigthsSet.Type

  object MelonDuo extends PhantomType[(Int, String)]
  type MelonDuo = MelonDuo.Type

  object MelonTrio extends PhantomType[(Int, String, List[Int])]
  type MelonTrio = MelonTrio.Type

}
