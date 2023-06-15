package com.algolia.instantsearch.searchbox.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searchbox.SearchMode

internal data class SearchBoxConnectionSearcher<R>(
    private val viewModel: SearchBoxViewModel,
    private val searcher: Searcher<R>,
    private val searchMode: SearchMode,
    private val debouncer: Debouncer,
    private val searchOnQueryUpdate: Boolean,
) : AbstractConnection() {

    private val searchAsYouType: Callback<String?> = { query ->
        searcher.setQuery(query)
        if (searchOnQueryUpdate) {
            debouncer.debounce(searcher) { searchAsync() }
        }
    }
    private val searchOnSubmit: Callback<String?> = { query ->
        searcher.setQuery(query)
        if (searchOnQueryUpdate) {
            searcher.searchAsync()
        }
    }

    override fun connect() {
        super.connect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.subscribe(searchAsYouType)
            SearchMode.OnSubmit -> viewModel.eventSubmit.subscribe(searchOnSubmit)
        }
    }

    override fun disconnect() {
        super.disconnect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.unsubscribe(searchAsYouType)
            SearchMode.OnSubmit -> viewModel.eventSubmit.unsubscribe(searchOnSubmit)
        }
    }
}
