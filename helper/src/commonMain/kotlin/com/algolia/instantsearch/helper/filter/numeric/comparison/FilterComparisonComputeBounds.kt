package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.Range
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats


private fun <T : Number> NumberViewModel<T>.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>,
    range: (FacetStats) -> Range<T>
) {
    facetStats[attribute]?.let {
        applyBounds(range(it))
    }
}

public fun NumberViewModel.Int.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Int(it.min.toInt(), it.max.toInt()) }
}

public fun NumberViewModel.Long.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Long(it.min.toLong(), it.max.toLong()) }
}

public fun NumberViewModel.Float.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Float(it.min, it.max) }
}

public fun NumberViewModel.Double.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Double(it.min.toDouble(), it.max.toDouble()) }
}