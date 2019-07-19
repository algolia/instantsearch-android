package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100)
) {
    if (searchAsYouType) {
        query.subscribe {
            searcher.setQuery(it)
            debouncer.debounce(searcher) { search() }
        }
    } else {
        eventSubmit.subscribe {
            searcher.setQuery(it)
            searcher.searchAsync()
        }
    }
}