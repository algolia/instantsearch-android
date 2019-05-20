package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel

fun Searcher.connectSearchBoxViewModel(
    searchBoxViewModel: SearchBoxViewModel,
    searchAsYouType: Boolean = true
) {
    val triggerSearch: (String?) -> Unit = {
        setQuery(it)
        search()
    }
    if (searchAsYouType) {
        searchBoxViewModel.changeListeners += triggerSearch
    } else {
        searchBoxViewModel.submitListeners += triggerSearch
    }
}