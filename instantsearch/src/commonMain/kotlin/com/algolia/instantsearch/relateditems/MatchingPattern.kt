package com.algolia.instantsearch.relateditems

import kotlin.reflect.KProperty1

/**
 * Representation of a scored filter based on a hit attribute.
 *
 * @param attribute hit's attribute
 * @param score filter score
 * @param property hit's property
 */
public data class MatchingPattern<T>(
    val attribute: String,
    val score: Int,
    val property: KProperty1<T, *>,
)
