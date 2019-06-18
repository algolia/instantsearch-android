package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats


private fun <T> NumberViewModel<T>.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>,
    transform: (Float) -> T
) where T : Number, T : Comparable<T> {
    facetStats[attribute]?.let {
        bounds = Range(transform(it.min), transform(it.max))
    }
}

public fun NumberViewModel<Int>.computeBoundsFromFacetStatsInt(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { it.toInt() }
}

public fun NumberViewModel<Long>.computeBoundsFromFacetStatsLong(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { it.toLong() }
}

public fun NumberViewModel<Float>.computeBoundsFromFacetStatsFloat(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { it }
}

public fun NumberViewModel<Double>.computeBoundsFromFacetStatsDouble(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { it.toDouble() }
}