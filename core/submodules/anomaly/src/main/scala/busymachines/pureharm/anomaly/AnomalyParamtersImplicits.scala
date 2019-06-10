package busymachines.pureharm.anomaly

import busymachines.pureharm.{Anomaly, SeqStringWrapper, StringWrapper}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  *
  */
trait AnomalyParamtersImplicits {
  implicit final def anomalyParamValueStringWrapper(s: String): Anomaly.Parameter =
    StringWrapper(s)

  implicit final def anomalyParamValueSeqOfStringWrapper(ses: Seq[String]): Anomaly.Parameter =
    SeqStringWrapper(ses.toVector)
}
