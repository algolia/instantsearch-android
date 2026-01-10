package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.migration2to3.FilterGroupsConverter
import com.algolia.instantsearch.searcher.SearcherForFacets

/**
 * Connection between facets searcher (searcher w/ query) and filter state.
 */
internal data class FacetsSearcherConnectionFilterState(
    private val searcher: SearcherForFacets<*>,
    private val filterState: FilterState,
    private val debouncer: Debouncer,
) : AbstractConnection() {

    private val updateSearcher: Callback<Filters> = { filters ->
        searcher.updateFilters(filters)
        debouncer.debounce(searcher as Searcher<*>) { searcher.searchAsync().join() }
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

    private fun SearcherForFacets<*>.updateFilters(filters: Filters = filterState) {
        query.filters = FilterGroupsConverter.SQL(filters.toFilterGroups())
    }
}
