package jp.riken.massbank.parser.scala.groups

import java.time.LocalDate
import jp.riken.massbank.parser.scala.types._

sealed trait MassBankGroup

case class RecordSpecificGroup(
  `ACCESSION`: Accession,
  `RECORD_TITLE`: Option[String],
  `DATE`: Option[LocalDate],
  `AUTHORS`: Option[String],
  `LICENSE`: Option[String],
  `COPYRIGHT`: Option[String],
  `PUBLICATION`: Option[String],
  `COMMENT`: Option[String]
) extends MassBankGroup

case class ChemicalGroup(
  `CH$NAME`: Option[String],
  `CH$COMPOUND_CLASS`: Option[String],
  `CH$FORMULA`: Option[String],
  `CH$EXACT_MASS`: Option[Double],
  `CH$SMILES`: Option[String],
  `CH$IUPAC`: Option[String],
  `CH$LINK`: List[DatabaseLink]
) extends MassBankGroup

case class SampleGroup(
  `SP$SCIENTIFIC_NAME`: Option[String],
  `SP$LINEAGE`: Option[String],
  `SP$LINK`: List[DatabaseLink],
  `SP$SAMPLE`: Option[String]
) extends MassBankGroup

case class AnalyticalChemistryGroup(
  `AC$INSTRUMENT`: Option[String],
  `AC$INSTRUMENT_TYPE`: Option[String],
  `AC$MASS_SPECTROMETRY: MS_TYPE`: Option[String],
  `AC$MASS_SPECTROMETRY: ION_MODE`: Option[String],
  `AC$MASS_SPECTROMETRY`: Option[String],
  `AC$CHROMATOGRAPHY`: Option[String]
) extends MassBankGroup

case class MassSpectralDataGroup(
  `MS$FOCUSED_ION`: Option[String],
  `MS$DATA_PROCESSING`: Option[String]
) extends MassBankGroup

case class MassSpectralPeakDataGroup(
  `PK$SPLASH`: Option[String],
  `PK$ANNOTATION`: Option[String],
  `PK$NUM_PEAK`: Int,
  `PK$PEAK`: PeakData
) extends MassBankGroup

