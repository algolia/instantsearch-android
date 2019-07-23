package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroupsConverter


internal class SearcherSingleConnectionFilterState(
    private val searcher: SearcherSingleIndex,
    private val filterState: FilterState,
    private val debouncer: Debouncer
) : ConnectionImpl() {

    private val updateSearcher: Callback<Filters> = { filters ->
        searcher.updateFilters(filters)
        debouncer.debounce(searcher) { searchAsync() }
    }

    init {
        searcher.updateFilters()
        searcher.disjunctive = {
            val disjunctiveAttributes = filterState.getFacetGroups()
                .filter { it.key.operator == FilterOperator.Or }
                .flatMap { group -> group.value.map { it.attribute } }

            disjunctiveAttributes to filterState.getFilters()
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribe(updateSearcher)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateSearcher)
    }

    private fun SearcherSingleIndex.updateFilters(filters: Filters = filterState) {
        query.filters = FilterGroupsConverter.SQL(filters.toFilterGroups())
    }
}