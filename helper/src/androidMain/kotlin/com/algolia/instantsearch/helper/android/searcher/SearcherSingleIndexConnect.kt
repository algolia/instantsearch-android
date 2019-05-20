package com.algolia.instantsearch.helper.android.searcher

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searcher.Searcher

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