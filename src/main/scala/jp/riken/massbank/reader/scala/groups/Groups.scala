package jp.riken.massbank.reader.scala.groups

import jp.riken.massbank.reader.scala.types._

// TODO: Make sure parser for solvent field is iterative
sealed trait MassBankGroup

case class RecordSpecificGroup(
  accession: Option[Accession],
  recordTitle: Option[String],
  date: Option[String],
  authors: Option[String],
  license: Option[String],
  copyright: Option[String],
  publication: Option[String],
  comment: List[String]
) extends MassBankGroup

case class ChemicalGroup(
  name: List[String],
  compoundClass: Option[String],
  formula: Option[String],
  exact_mass: Option[Double],
  smiles: Option[String],
  iupac: Option[String],
  link: Map[String, String]
) extends MassBankGroup

case class SampleGroup(
  scientificName: Option[String],
  lineage: Option[String],
  link: Map[String, String],
  sample: Option[String]
) extends MassBankGroup

case class AnalyticalChemistryGroup(
    instrument: Option[String],
    instrumentType: Option[String],
    massSpectrometry: Map[String, String],
    chromatography: Map[String, String]
) extends MassBankGroup {
  lazy val ionMode: Option[String] = massSpectrometry.get("ION_MODE")
  lazy val msType: Option[String] = massSpectrometry.get("MS_TYPE")

}

case class MassSpectralDataGroup(
  focusedIon: Map[String, String],
  dataProcessing: Map[String, String]
) extends MassBankGroup

case class MassSpectralPeakDataGroup(
  splash: Option[String],
  annotation: Option[String],
  numPeak: Int,
  peak: PeakData
) extends MassBankGroup

