package com.algolia.instantsearch.searcher.internal

import com.algolia.client.model.search.SearchParamsObject
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.filter.FilterGroupsConverter
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.state.toFilterGroups
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
        val filterString = FilterGroupsConverter.SQL(filters.toFilterGroups())
        val typedSearcher = this as? SearcherForFacets<SearchParamsObject> ?: return
        typedSearcher.query = typedSearcher.query.copy(filters = filterString)
    }
}
