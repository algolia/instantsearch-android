package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searcher.Searcher

inline fun SearchBoxViewModel.connectSearcher(
    searcher: Searcher,
    searchAsYouType: Boolean = true,
    crossinline queryHook: QueryHook = { _ -> true }
) {
    val triggerSearch: (String?) -> Unit = {
        searcher.setQuery(it)
        if (queryHook(it)) {
            searcher.search()
        }
    }
    if (searchAsYouType) {
        changeListeners = changeListeners + triggerSearch
    } else {
        submitListeners = submitListeners + triggerSearch
    }
}