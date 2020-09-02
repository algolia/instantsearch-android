package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroupsConverter

internal data class SearcherMultipleConnectionFilterState(
    private val searcher: SearcherMultipleIndex,
    private val filterState: FilterState,
    private val indexName: IndexName,
    private val debouncer: Debouncer,
) : ConnectionImpl() {

    private val updateSearcher: Callback<Filters> = { filters ->
        searcher.updateFilters(filters)
        debouncer.debounce(searcher) { searcher.searchAsync().join() }
    }

    private val index = searcher.queries.indexOfFirst { it.indexName == indexName }

    init {
        require(index != -1) {
            "No Index \"$indexName\" present in searcher current indices: ${searcher.queries.map { it.indexName }}."
        }
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

    private fun SearcherMultipleIndex.updateFilters(filters: Filters = filterState) {
        queries[index].query.filters = FilterGroupsConverter.SQL(filters.toFilterGroups())
    }
}
