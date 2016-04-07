package jp.riken.massbank.reader.scala.types

trait Peak
case class CompletePeakTriple(mz: Double, absInt: Double, relInt: Double) extends Peak
case class AbsolutePeakPair(mz: Double, absInt: Double) extends Peak
case class RelativePeakPair(mz: Double, relInt: Double) extends Peak

case class PeakData(preprocessedPeaks: List[Peak]) {
  private def asCompleteTriples(ps: List[Peak]): List[CompletePeakTriple] = ps.map {
    case p: CompletePeakTriple  => p
    case AbsolutePeakPair(m, i) => CompletePeakTriple(m, i, i)
    case RelativePeakPair(m, r) => CompletePeakTriple(m, r, r)
  }

  private def computeRelativeIntensities(ps: List[CompletePeakTriple]): List[CompletePeakTriple] = {
    val x = ps.map(_.absInt).max
    ps.map(p => p.copy(relInt = math.round(p.absInt / x * 999f)))
  }

  private def computePeaks(ps: List[Peak]): List[CompletePeakTriple] = ps.head match {
    case _: CompletePeakTriple => ps.asInstanceOf[List[CompletePeakTriple]]
    case _                     => computeRelativeIntensities(asCompleteTriples(ps))
  }

  lazy val peaks: List[CompletePeakTriple] = computePeaks(preprocessedPeaks)
}

