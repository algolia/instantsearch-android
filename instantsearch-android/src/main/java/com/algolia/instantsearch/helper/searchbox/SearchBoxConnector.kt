package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceSearchInMillis

public data class SearchBoxConnector<R>(
    public val searcher: Searcher<R>,
    public val viewModel: SearchBoxViewModel = SearchBoxViewModel(),
    public val searchMode: SearchMode = SearchMode.AsYouType,
    public val debouncer: Debouncer = Debouncer(debounceSearchInMillis),
) : ConnectionImpl() {

    private val connectionSearcher = viewModel.connectSearcher(searcher, searchMode, debouncer)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
