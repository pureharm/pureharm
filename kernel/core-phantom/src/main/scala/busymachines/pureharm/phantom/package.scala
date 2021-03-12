package busymachines.pureharm

package object phantom {
  type Sprout[O]                = sprout.Sprout[O]
  type SproutSub[O]             = sprout.SproutSub[O]
  type SproutRefined[O, E]      = sprout.SproutRefined[O, E]
  type SproutRefinedSub[O, E]   = sprout.SproutRefinedSub[O, E]
  type SproutRefinedThrow[O]    = sprout.SproutRefinedThrow[O]
  type SproutRefinedSubThrow[O] = sprout.SproutRefinedSub[O, Throwable]

  type OldType[O, N] = sprout.OldType[O, N]
  val OldType: sprout.OldType.type = sprout.OldType
  type NewType[O, N] = sprout.NewType[O, N]
  val NewType: sprout.NewType.type = sprout.NewType
  type RefinedType[O, N, E] = sprout.RefinedType[O, N, E]
  val RefinedType: sprout.RefinedType.type = sprout.RefinedType
  type RefinedTypeThrow[O, N] = sprout.RefinedTypeThrow[O, N]
  val RefinedTypeThrow: sprout.RefinedTypeThrow.type = sprout.RefinedTypeThrow
}
