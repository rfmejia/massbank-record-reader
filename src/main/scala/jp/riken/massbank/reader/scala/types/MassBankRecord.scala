package jp.riken.massbank.reader.scala.types

import jp.riken.massbank.reader.scala.groups._

case class MassBankRecord(
  recordSpecificGroup: Option[RecordSpecificGroup],
  chemicalGroup: Option[ChemicalGroup],
  sampleGroup: Option[SampleGroup],
  analyticalChemistryGroup: Option[AnalyticalChemistryGroup],
  massSpectralDataGroup: Option[MassSpectralDataGroup],
  massSpectraPeakDataGroup: Option[MassSpectralPeakDataGroup]
)
