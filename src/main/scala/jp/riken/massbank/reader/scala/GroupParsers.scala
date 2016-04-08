package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.groups._
import jp.riken.massbank.reader.scala.types._

import scala.util.Try

trait RecordSpecificGroupParser extends FieldParsers {
  def recordSpecificGroup =
    fieldsStartingWith("") ^^ {
      case fields =>
        RecordSpecificGroup(
          fields.getValue("ACCESSION").map(_.asInstanceOf[Accession]),
          fields.getValue("RECORD_TITLE"),
          fields.getValue("DATE"),
          fields.getValue("AUTHORS"),
          fields.getValue("LICENSE"),
          fields.getValue("COPYRIGHT"),
          fields.getValue("PUBLICATION"),
          fields.getIterative("COMMENT"),
          fields -- List("ACCESSION", "RECORD_TITLE", "DATE", "AUTHORS", "LICENSE", "COPYRIGHT", "PUBLICATION", "COMMENT")
        )
    }
}
object RecordSpecificGroupParser extends RecordSpecificGroupParser

trait ChemicalGroupParser extends FieldParsers {
  def chemicalGroup =
    fieldsStartingWith("CH$") ^^ {
      case fields =>
        ChemicalGroup(
          fields.getIterative("CH$NAME"),
          fields.getValue("CH$COMPOUND_CLASS"),
          fields.getValue("CH$FORMULA"),
          fields.getValue("CH$EXACT_MASS"),
          fields.getValue("CH$SMILES"),
          fields.getValue("CH$IUPAC"),
          fields.getSubtags("CH$LINK"),
          fields -- List("CH$NAME", "CH$COMPOUND_CLASS", "CH$FORMULA", "CH$EXACT_MASS", "CH$SMILES", "CH$IUPAC", "CH$LINK")
        )
    }
}
object ChemicalGroupParser extends ChemicalGroupParser

trait SampleGroupParser extends FieldParsers {
  def sampleGroup =
    fieldsStartingWith("SP$") ^^ {
      case fields =>
        SampleGroup(
          fields.getValue("SP$SCIENTIFIC_NAME"),
          fields.getValue("SP$LINEAGE"),
          fields.getSubtags("SP$LINK"),
          fields.getValue("SP$SAMPLE"),
          fields -- List("SP$SCIENTIFIC_NAME", "SP$LINEAGE", "SP$LINK", "SP$SAMPLE")
        )
    }
}
object SampleGroupParser extends ChemicalGroupParser

trait AnalyticalChemistryGroupParser extends FieldParsers {
  def analyticalChemistryGroup =
    fieldsStartingWith("AC$") ^^ {
      case fields =>
        AnalyticalChemistryGroup(
          fields.getValue("AC$INSTRUMENT"),
          fields.getValue("AC$INSTRUMENT_TYPE"),
          fields.getSubtags("AC$MASS_SPECTROMETRY"),
          fields.getSubtags("AC$CHROMATOGRAPHY"),
          fields -- List("AC$INSTRUMENT", "AC$INSTRUMENT_TYPE", "AC$MASS_SPECTROMETRY", "AC$CHROMATOGRAPHY")
        )
    }
}
object AnalyticalChemistryGroupParser extends AnalyticalChemistryGroupParser

trait MassSpectralDataGroupParser extends FieldParsers {
  def massSpectralDataGroup =
    fieldsStartingWith("MS$") ^^ {
      case fields =>
        MassSpectralDataGroup(
          fields.getSubtags("MS$FOCUSED_ION"),
          fields.getSubtags("MS$DATA_PROCESSING"),
          fields -- List("MS$FOCUSED_ION", "MS$DATA_PROCESSING")
        )
    }
}
object MassSpectralDataGroupParser extends MassSpectralDataGroupParser

trait MassSpectralPeakDataGroupParser extends FieldParsers {
  def massSpectralPeakDataGroup =
    fieldsStartingWith("PK$") ~ peakTriple.+ ^^ {
      case fields ~ peaks =>
        println(fields.mkString("\n"))
        val numPeak = fields.getValue("PK$NUM_PEAK").flatMap(s => Try(s.toInt).toOption)
        val others = fields -- List("PK$SPLASH", "PK$ANNOTATION")

        MassSpectralPeakDataGroup(
          fields.getValue("PK$SPLASH"),
          fields.getValue("PK$ANNOTATION"),
          numPeak.getOrElse(0),
          PeakData(peaks),
          others
        )
    }
}
object MassSpectralPeakDataGroupParser extends MassSpectralPeakDataGroupParser
