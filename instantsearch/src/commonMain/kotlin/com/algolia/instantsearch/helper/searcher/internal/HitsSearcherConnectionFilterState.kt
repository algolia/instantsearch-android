package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searcher.FilterGroupsHolder
import com.algolia.instantsearch.helper.searcher.SearcherQuery
import com.algolia.search.model.filter.FilterGroupsConverter

/**
 * Connection between Hits Searcher (searcher w/ query and filterGroups) and filter state.
 */
internal data class HitsSearcherConnectionFilterState<S, R>(
    private val searcher: S,
    private val filterState: FilterState,
    private val debouncer: Debouncer,
) : ConnectionImpl() where S : SearcherQuery<*, R>, S : FilterGroupsHolder {

    private val updateSearcher: Callback<Filters> = { filters ->
        searcher.updateFilters(filters)
        debouncer.debounce(searcher) { searchAsync() }
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

    private fun S.updateFilters(filters: Filters = filterState) {
        filterGroups = filters.toFilterGroups()
        query.filters = FilterGroupsConverter.SQL(filterGroups)
    }
}
