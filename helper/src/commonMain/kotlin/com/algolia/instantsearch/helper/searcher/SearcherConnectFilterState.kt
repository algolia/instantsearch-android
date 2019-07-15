package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroupsConverter


public fun SearcherSingleIndex.connectFilterState(filterState: FilterState, key: ObservableKey? = null) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    computeDisjunctiveParams = {
        val disjunctiveAttributes = filterState.getFacetGroups()
            .filter { it.key.operator == FilterOperator.Or }
            .flatMap { group -> group.value.map { it.attribute } }

        hierarchicalAttributes = filterState.hierarchicalAttributes
        hierarchicalFilters = filterState.hierarchicalFilters
        disjunctiveAttributes to filterState.getFilters()
    }
    filterState.filters.subscribe(key) {
        updateFilters()
        searchAsync()
    }
}

public fun SearcherForFacets.connectFilterState(filterState: FilterState, key: ObservableKey? = null) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    filterState.filters.subscribe(key) {
        updateFilters()
        searchAsync()
    }
}