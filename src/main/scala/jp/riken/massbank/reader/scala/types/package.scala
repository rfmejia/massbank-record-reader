package jp.riken.massbank.reader.scala

package object types {
  type Tagged[U] = { type Tag = U }
  type @@[T, U] = T with Tagged[U]

  protected object definitions {
    trait Accession
    trait DatabaseLink
  }

  type Accession = String @@ definitions.Accession
  type DatabaseLink = (String, String) @@ definitions.DatabaseLink
}

