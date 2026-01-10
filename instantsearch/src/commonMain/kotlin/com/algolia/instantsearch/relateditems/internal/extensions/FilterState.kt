package com.algolia.instantsearch.relateditems.internal.extensions

import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.migration2to3.Filter
import com.algolia.instantsearch.migration2to3.FilterGroup
import com.algolia.instantsearch.relateditems.MatchingPattern

internal fun <T> FilterState.addMatchingPattern(hit: T, matchingPattern: MatchingPattern<T>) {
    val optionalFilter = matchingPattern.toOptionalFilter(hit)
    optionalFilter?.let {
        add(it.filterGroupID, *it.filterFacets)
    }
}

internal fun FilterState.toFilterFacetGroup(): Set<FilterGroup<Filter.Facet>> {
    return getFacetGroups().map { (key, value) ->
        when (key.operator) {
            FilterOperator.And -> FilterGroup.And.Facet(value, key.name)
            FilterOperator.Or -> FilterGroup.Or.Facet(value, key.name)
        }
    }.toSet()
}
