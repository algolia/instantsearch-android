package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


public fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setQuery(query)
    searchBoxView.onQueryChanged = { query = it }
    searchBoxView.onQuerySubmitted = {
        query = it
        submitQuery()
    }
}