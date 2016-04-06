package jp.riken.massbank.reader.scala.types

trait Peak
case class CompletePeakTriple(mz: Double, int: Double, rel: Double) extends Peak
case class AbsolutePeakPair(mz: Double, int: Double) extends Peak
case class RelativePeakPair(mz: Double, rel: Double) extends Peak

case class PeakData(peaks: List[Peak]) {
  val hasRelativeIntensity: Boolean = false
  val hasAbsoluteIntensity: Boolean = false
}
