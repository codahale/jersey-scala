package com.codahale.jersey

import scala.collection.mutable.Builder

trait CollectionBuilder[X <: Object] {
  def apply(): Builder[String, X]
}

object SeqCollectionBuilder extends CollectionBuilder[Seq[String]] {
  def apply() = Seq.newBuilder[String]
}

object ListCollectionBuilder extends CollectionBuilder[List[String]] {
  def apply() = List.newBuilder[String]
}

object VectorCollectionBuilder extends CollectionBuilder[Vector[String]] {
  def apply() = Vector.newBuilder[String]
}

object IndexedSeqCollectionBuilder extends CollectionBuilder[IndexedSeq[String]] {
  def apply() = IndexedSeq.newBuilder[String]
}

object SetCollectionBuilder extends CollectionBuilder[Set[String]] {
  def apply() = Set.newBuilder[String]
}
