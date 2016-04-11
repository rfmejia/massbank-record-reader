## MassBank Record Reader

**massbank-record-reader** is a parser combinator library for the [MassBank](http://www.massbank.jp) Record Format. The library is written in Scala, has APIs for Scala and (coming soon) Java.

This covers the specification version **2.10** released on *March 1, 2016*.

To be consistent, versioning of releases will follow the same value as the MassBank Record Format specification (available for download on their website), followed by a integer version number for the library implementation.

### Quick usage

Download a release JAR and add the library in your project classpath. Then, use the reader in your code:

```scala
import jp.riken.massbank.reader.scala.MassBankRecordReader
import jp.riken.massbank.reader.scala.types.MassBankRecord

val lines: String = // MassBank record as a string
val result = MassBankRecordReader.read(lines)
result match {
  case Success(record) => // Parsing successful
  case Failure(err)  => // Encountered an error, summarized in exception
}
```

When parsing is successful, a `MassBankRecord` parse tree is produced. The parse tree is a set of named properties as defined in the 2.10 specifications. Currently, only 'PK$NUM_PEAK' and 'PK$PEAK' have special types; other types are all parsed as a String, and are stored as an Option[String]. List[String], or Map[String, String].
