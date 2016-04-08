package jp.riken.massbank.reader.scala.types

case class PeakTriple(mz: Double, absInt: Double, relInt: Double)

case class PeakData(preprocessedPeaks: List[PeakTriple]) {
  private def computeRelativeIntensities(ps: List[PeakTriple]): List[PeakTriple] = {
    val x = ps.map(_.absInt).max
    ps.map(p => p.copy(relInt = math.round(p.absInt / x * 999f)))
  }

  lazy val peaks: List[PeakTriple] = computeRelativeIntensities(preprocessedPeaks)
}

object PeakData {
  val empty = PeakData(List.empty)
}
