package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


public fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setItem(item)
    searchBoxView.onQueryChanged = { item = it }
    searchBoxView.onQuerySubmitted = {
        item = it
        submitQuery()
    }
}