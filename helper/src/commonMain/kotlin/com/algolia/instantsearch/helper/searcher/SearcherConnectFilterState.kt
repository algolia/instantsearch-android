package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroupsConverter


fun SearcherSingleIndex.connectFilterState(filterState: FilterState) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    filterState.onChanged += {
        updateFilters()
        search()
    }
}

fun SearcherForFacets.connectFilterState(filterState: FilterState) {

    fun updateFilters() {
        query.filters = FilterGroupsConverter.SQL(filterState.toFilterGroups())
    }

    updateFilters()
    filterState.onChanged += {
        updateFilters()
        search()
    }
}