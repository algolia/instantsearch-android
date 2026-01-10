package com.algolia.instantsearch.relateditems.internal.extensions

import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.migration2to3.Filter
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.internal.FilterFacetAndID

internal fun <T> List<MatchingPattern<T>>.toOptionalFilters(hit: T): List<List<String>>? {
    val filterState = FilterState()
    forEach { filterState.addMatchingPattern(hit, it) }
    return FilterGroupsConverter.Legacy.Facet(filterState.toFilterFacetGroup()).unquote()
}

/**
 * Create an [FilterFacetAndID] from a [MatchingPattern].
 */
internal fun <T> MatchingPattern<T>.toOptionalFilter(hit: T): FilterFacetAndID? {
    val property = property.get(hit) ?: return null
    return when (property) {
        is Iterable<*> -> {
            val groupOr = groupOr()
            val list = property.map { value -> Filter.Facet(attribute, value.toString(), score) }.toTypedArray()
            FilterFacetAndID(groupOr, list)
        }
        else -> FilterFacetAndID(
            groupAnd(),
            arrayOf(Filter.Facet(attribute, property.toString(), score))
        )
    }
}
