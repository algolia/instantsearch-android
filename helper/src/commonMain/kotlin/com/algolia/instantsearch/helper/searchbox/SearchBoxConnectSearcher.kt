package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100),
    key: ObservableKey? = null
) {
    if (searchAsYouType) {
        query.subscribe {
            searcher.setQuery(it)
            debouncer.debounce(searcher) { search() }
        }
    } else {
        event.subscribe {
            searcher.setQuery(it)
            searcher.searchAsync()
        }
    }
}