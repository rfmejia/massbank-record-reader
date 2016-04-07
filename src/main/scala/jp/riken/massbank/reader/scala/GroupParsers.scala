package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.groups._
import jp.riken.massbank.reader.scala.types._

trait RecordSpecificGroupParser extends FieldParsers {
  def recordSpecificGroup =
    stringField("ACCESSION").? ~
      stringField("RECORD_TITLE").? ~
      stringField("DATE").? ~
      stringField("AUTHORS").? ~
      stringField("LICENSE").? ~
      stringField("COPYRIGHT").? ~
      stringField("PUBLICATION").? ~
      stringField("COMMENT").* ^^ {
        case accession ~ recordTitle ~ date ~ authors ~ license ~ copyright ~ publication ~ comment =>
          RecordSpecificGroup(accession.map(_.asInstanceOf[Accession]), recordTitle, date, authors, license, copyright, publication, comment)
      }
}
object RecordSpecificGroupParser extends RecordSpecificGroupParser

trait ChemicalGroupParser extends FieldParsers {
  def chemicalGroup =
    stringField("CH$NAME").* ~
      stringField("CH$COMPOUND_CLASS").? ~
      stringField("CH$FORMULA").? ~
      stringField("CH$EXACT_MASS").? ~
      stringField("CH$SMILES").? ~
      stringField("CH$IUPAC").? ~
      subtagField("CH$LINK").* ^^ {
        case name ~ compoundClass ~ formula ~ exactMass ~ smiles ~ iupac ~ links =>
          ChemicalGroup(name, compoundClass, formula, exactMass, smiles, iupac, links.toMap)
      }
}
object ChemicalGroupParser extends ChemicalGroupParser

trait SampleGroupParser extends FieldParsers {
  def sampleGroup =
    stringField("SP$SCIENTIFIC_NAME").? ~
      stringField("SP$LINEAGE").? ~
      subtagField("SP$LINK").* ~
      stringField("SP$SAMPLE").? ^^ {
        case scientificName ~ lineage ~ links ~ sample =>
          SampleGroup(scientificName, lineage, links.toMap, sample)
      }
}
object SampleGroupParser extends ChemicalGroupParser

trait AnalyticalChemistryGroupParser extends FieldParsers {
  def analyticalChemistryGroup =
    stringField("AC$INSTRUMENT").? ~
      stringField("AC$INSTRUMENT_TYPE").? ~
      subtagField("AC$MASS_SPECTROMETRY").* ~
      subtagField("AC$CHROMATOGRAPHY").* ^^ {
        case instrument ~ instrumentType ~ massSpectrometry ~ chromatography =>
          AnalyticalChemistryGroup(instrument, instrumentType, massSpectrometry.toMap, chromatography.toMap)
      }
}
object AnalyticalChemistryGroupParser extends AnalyticalChemistryGroupParser

trait MassSpectralDataGroupParser extends FieldParsers {
  def massSpectralDataGroup =
    subtagField("MS$FOCUSED_ION").* ~
      subtagField("MS$DATA_PROCESSING").* ^^ {
        case focusedIon ~ dataProcessing => MassSpectralDataGroup(focusedIon.toMap, dataProcessing.toMap)
      }
}
object MassSpectralDataGroupParser extends MassSpectralDataGroupParser

trait MassSpectralPeakDataGroupParser extends FieldParsers {
  def massSpectralPeakDataGroup =
    stringField("PK$SPLASH").? ~
      stringField("PK$ANNOTATION").? ~
      numPeakField("PK$NUM_PEAK") ~
      peakField("PK$PEAK") ^^ {
        case splash ~ annotation ~ numPeak ~ peaks =>
          numPeak.map(num => MassSpectralPeakDataGroup(splash, annotation, num, peaks))
            .getOrElse(MassSpectralPeakDataGroup(splash, annotation, 0, PeakData(List.empty)))
      }

}
object MassSpectralPeakDataGroupParser extends MassSpectralPeakDataGroupParser
