package com.algolia.instantsearch.filter.numeric.comparison

import com.algolia.client.model.search.FacetStats
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.filter.range.internal.mapperOf

private fun <T> NumberViewModel<T>.setBoundsFromFacetStats(
    attribute: String,
    facetStats: Map<String, FacetStats>,
    transform: (Number) -> T,
) where T : Number, T : Comparable<T> {
    val stats = facetStats[attribute] ?: return
    val min = stats.min
    val max = stats.max
    if (min == null || max == null) return
    bounds.value = Range(transform(min), transform(max))
}

public fun NumberViewModel<Int>.setBoundsFromFacetStatsInt(
    attribute: String,
    facetStats: Map<String, FacetStats>,
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toInt() }
}

public fun NumberViewModel<Long>.setBoundsFromFacetStatsLong(
    attribute: String,
    facetStats: Map<String, FacetStats>,
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toLong() }
}

public fun NumberViewModel<Float>.setBoundsFromFacetStatsFloat(
    attribute: String,
    facetStats: Map<String, FacetStats>,
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toFloat() }
}

public fun NumberViewModel<Double>.setBoundsFromFacetStatsDouble(
    attribute: String,
    facetStats: Map<String, FacetStats>,
) {
    setBoundsFromFacetStats(attribute, facetStats) { it.toDouble() }
}

/**
 * Set filter bounds from [FacetStats].
 *
 * @param attribute attribute to get its bounds
 * @param facetStats facet stats to get bounds from
 */
public inline fun <reified T> FilterComparisonConnector<T>.setBoundsFromFacetStats(
    attribute: String,
    facetStats: Map<String, FacetStats>
) where T : Number, T : Comparable<T> {
    setBoundsFromFacetStats(attribute, facetStats, mapperOf(T::class))
}

/**
 * Set filter bounds from [FacetStats].
 *
 * @param attribute attribute to get its bounds
 * @param facetStats facet stats to get bounds from
 * @param transform mapper from [Number] to [T]
 */
public fun <T> FilterComparisonConnector<T>.setBoundsFromFacetStats(
    attribute: String,
    facetStats: Map<String, FacetStats>,
    transform: (Number) -> T,
) where T : Number, T : Comparable<T> {
    viewModel.setBoundsFromFacetStats(attribute, facetStats, transform)
}
