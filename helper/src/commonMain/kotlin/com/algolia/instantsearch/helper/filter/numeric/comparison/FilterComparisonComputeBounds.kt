@file:JvmName("ComputeBounds")

package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats
import kotlin.jvm.JvmName


private fun <T> NumberViewModel<T>.setBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>,
    transform: (Float) -> T
) where T : Number, T : Comparable<T> {
    facetStats[attribute]?.let {
        bounds.value = Range(transform(it.min), transform(it.max))
    }
}

/**
 * Compute bounds for this NumberViewModel based on the given FacetStats for its attribute.
 */
public fun NumberViewModel<Int>.setBoundsFromFacetStatsInt(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toInt() }
}

/**
 * Compute bounds for this NumberViewModel based on the given FacetStats for its attribute.
 */
public fun NumberViewModel<Long>.setBoundsFromFacetStatsLong(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toLong() }
}

/**
 * Compute bounds for this NumberViewModel based on the given FacetStats for its attribute.
 */
public fun NumberViewModel<Float>.setBoundsFromFacetStatsFloat(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    setBoundsFromFacetStats(attribute, facetStats) { it }
}

/**
 * Compute bounds for this NumberViewModel based on the given FacetStats for its attribute.
 */
public fun NumberViewModel<Double>.setBoundsFromFacetStatsDouble(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toDouble() }
}