package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.core.selectable.range.Range
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats


private fun <T : Number> SelectableNumberViewModel<T>.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>,
    range: (FacetStats) -> Range<T>
) {
    facetStats[attribute]?.let {
        computeBounds(range(it))
    }
}

public fun SelectableNumberViewModel.Int.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Int(it.min.toInt(), it.max.toInt()) }
}

public fun SelectableNumberViewModel.Long.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Long(it.min.toLong(), it.max.toLong()) }
}

public fun SelectableNumberViewModel.Float.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Float(it.min, it.max) }
}

public fun SelectableNumberViewModel.Double.computeBoundsFromFacetStats(
    attribute: Attribute,
    facetStats: Map<Attribute, FacetStats>
) {
    computeBoundsFromFacetStats(attribute, facetStats) { Range.Double(it.min.toDouble(), it.max.toDouble()) }
}