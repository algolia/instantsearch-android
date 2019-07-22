package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroupsConverter


internal class SearcherForFacetsConnectionFilterState(
    private val searcher: SearcherForFacets,
    private val filterState: FilterState
) : ConnectionImpl() {

    private val updateSearcher: Callback<Filters> = { filters ->
        searcher.updateFilters(filters)
        searcher.searchAsync()
    }

    init {
        searcher.updateFilters()
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribe(updateSearcher)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateSearcher)
    }

    private fun SearcherForFacets.updateFilters(filters: Filters = filterState) {
        query.filters = FilterGroupsConverter.SQL(filters.toFilterGroups())
    }
}