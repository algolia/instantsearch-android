package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


internal class SearchBoxConnectionSearcher<R>(
    private val viewModel: SearchBoxViewModel,
    private val searcher: Searcher<R>,
    private val searchAsYouType: Boolean,
    private val debouncer: Debouncer
) : ConnectionImpl() {

    private val search: Callback<String?> = { query ->
        searcher.setQuery(query)
        debouncer.debounce(searcher) { search() }
    }
    private val searchOnSubmit: Callback<String?> = { query ->
        searcher.setQuery(query)
        searcher.searchAsync()
    }

    override fun connect() {
        super.connect()
        if (searchAsYouType) {
            viewModel.query.subscribe(search)
        } else {
            viewModel.eventSubmit.subscribe(searchOnSubmit)
        }
    }

    override fun disconnect() {
        super.disconnect()
        if (searchAsYouType) {
            viewModel.query.unsubscribe(search)
        } else {
            viewModel.eventSubmit.unsubscribe(searchOnSubmit)
        }
    }
}