package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searcher.Searcher

fun SearchBoxViewModel.connectSearcher(
    searcher: Searcher,
    searchAsYouType: Boolean = true
) {
    val triggerSearch: (String?) -> Unit = {
        searcher.setQuery(it)
        searcher.search()
    }
    if (searchAsYouType) {
        changeListeners = changeListeners + triggerSearch
    } else {
        submitListeners = submitListeners + triggerSearch
    }
}