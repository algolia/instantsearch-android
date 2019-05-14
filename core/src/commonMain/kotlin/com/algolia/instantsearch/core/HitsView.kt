package com.algolia.instantsearch.core

interface HitsView<T> {
    fun setHits(hits: List<T>)
}