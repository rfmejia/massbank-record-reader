package jp.riken.massbank.reader.scala.types

import jp.riken.massbank.reader.scala.groups._

case class MassBankRecord(
  recordSpecificGroup: RecordSpecificGroup,
  chemicalGroup: ChemicalGroup,
  sampleGroup: SampleGroup,
  analyticalChemistryGroup: AnalyticalChemistryGroup,
  massSpectralDataGroup: MassSpectralDataGroup,
  massSpectraPeakDataGroup: MassSpectralPeakDataGroup
)
