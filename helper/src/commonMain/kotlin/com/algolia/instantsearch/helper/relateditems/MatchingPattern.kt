package com.algolia.instantsearch.helper.relateditems

import com.algolia.search.model.Attribute
import kotlin.reflect.KProperty1

/**
 * Representation of a scored filter based on a hit attribute.
 *
 * @param attribute hit's attribute
 * @param score filter score
 * @param property hit's property
 */
data class MatchingPattern<T>(
    val attribute: Attribute,
    val score: Int,
    val property: KProperty1<T, *>
)
