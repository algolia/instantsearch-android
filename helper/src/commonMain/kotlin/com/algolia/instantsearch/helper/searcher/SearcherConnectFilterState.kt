package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroupsConverter


fun SearcherSingleIndex.connectFilterState(filterState: FilterState) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    disjunctive = {
        val disjunctiveAttributes = filterState.getFacetGroups()
            .filter { it.key.operator == FilterOperator.Or }
            .flatMap { group -> group.value.map { it.attribute } }

        hierarchicalAttributes = filterState.hierarchicalAttributes
        hierarchicalFilters = filterState.hierarchicalFilters
        disjunctiveAttributes to filterState.getFilters()
    }
    filterState.onChanged += {
        updateFilters()
        searchAsync()
    }
}

fun SearcherForFacets.connectFilterState(filterState: FilterState) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    filterState.onChanged += {
        updateFilters()
        searchAsync()
    }
}