package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.groups._
import jp.riken.massbank.reader.scala.types._

trait GroupParser[G <: MassBankGroup] extends LiteralParsers {
  def structure: Parser[_]

  def parse: Parser[G]
}

object RecordSpecificGroupParser extends GroupParser[RecordSpecificGroup] {
  def structure =
    stringField("ACCESSION") ~
      stringField("RECORD_TITLE").? ~
      dateField("DATE").? ~
      stringField("AUTHORS").? ~
      stringField("LICENSE").? ~
      stringField("COPYRIGHT").? ~
      stringField("PUBLICATION").? ~
      stringField("COMMENT").?

  def parse = structure ^^ {
    case accession ~ recordTitle ~ date ~ authors ~ license ~ copyright ~ publication ~ comment =>
      RecordSpecificGroup(accession.asInstanceOf[Accession], recordTitle, date, authors, license, copyright, publication, comment)
  }
}

object ChemicalGroupParser extends GroupParser[ChemicalGroup] {
  def structure =
    stringField("CH$NAME").? ~
      stringField("CH$COMPOUND_CLASS").? ~
      stringField("CH$FORMULA").? ~
      doubleField("CH$EXACT_MASS").? ~
      stringField("CH$SMILES").? ~
      stringField("CH$IUPAC").? ~
      dbLinkField("CH$LINK").*

  def parse = structure ^^ {
    case name ~ compoundClass ~ formula ~ exactMass ~ smiles ~ iupac ~ links =>
      ChemicalGroup(name, compoundClass, formula, exactMass, smiles, iupac, links)
  }
}

object SampleGroupParser extends GroupParser[SampleGroup] {
  def structure =
    stringField("SP$SCIENTIFIC_NAME").? ~
      stringField("SP$LINEAGE").? ~
      dbLinkField("SP$LINK").* ~
      stringField("SP$SAMPLE").?

  def parse = structure ^^ {
    case scientificName ~ lineage ~ links ~ sample =>
      SampleGroup(scientificName, lineage, links, sample)
  }
}

object AnalyticalChemistryGroupParser extends GroupParser[AnalyticalChemistryGroup] {
  def structure =
    stringField("AC$INSTRUMENT").? ~
      stringField("AC$INSTRUMENT_TYPE").? ~
      stringField("AC$MASS_SPECTROMETRY: MS_TYPE").? ~
      stringField("AC$MASS_SPECTROMETRY: ION_MODE").? ~
      stringField("AC$MASS_SPECTROMETRY").? ~
      stringField("AC$CHROMATOGRAPHY").?

  def parse = structure ^^ {
    case instrument ~ instrumentType ~ msType ~ ionMode ~ massSpectrometry ~ chromatography =>
      AnalyticalChemistryGroup(instrument, instrumentType, msType, ionMode, massSpectrometry, chromatography)
  }
}

object MassSpectralDataGroupParser extends GroupParser[MassSpectralDataGroup] {
  def structure =
    stringField("MS$FOCUSED_ION").? ~
      stringField("MS$DATA_PROCESSING").?

  def parse = structure ^^ {
    case focusedIon ~ dataProcessing => MassSpectralDataGroup(focusedIon, dataProcessing)
  }
}

object MassSpectralPeakDataGroupParser extends GroupParser[MassSpectralPeakDataGroup] {
  def structure =
    stringField("PK$SPLASH").? ~
      stringField("PK$ANNOTATION").? ~
      intField("PK$NUM_PEAK") ~
      peakField("PK$PEAK")

  def parse = structure ^^ {
    case splash ~ annotation ~ numPeak ~ peaks => MassSpectralPeakDataGroup(splash, annotation, numPeak, peaks)
  }
}
