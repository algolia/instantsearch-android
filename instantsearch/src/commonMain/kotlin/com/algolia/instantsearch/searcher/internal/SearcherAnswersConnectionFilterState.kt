@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.searcher.SearcherAnswers
import com.algolia.search.ExperimentalAlgoliaClientAPI
import com.algolia.search.model.filter.FilterGroupsConverter

@Deprecated("Answers feature is deprecated")
@ExperimentalInstantSearch
internal data class SearcherAnswersConnectionFilterState(
    private val searcher: SearcherAnswers,
    private val filterState: FilterState,
    private val debouncer: Debouncer,
) : AbstractConnection() {

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

    @OptIn(ExperimentalAlgoliaClientAPI::class)
    private fun SearcherAnswers.updateFilters(filters: Filters = filterState) {
        val filterGroups = filters.toFilterGroups()
        query.filters = FilterGroupsConverter.SQL(filterGroups)
    }
}
