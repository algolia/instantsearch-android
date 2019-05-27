package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


public fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.onQueryChanged = { query = it }
    searchBoxView.onQuerySubmitted = {
        query = it
        submitQuery()
    }
    val onQueryChange: (String?) -> Unit = {
        searchBoxView.setQuery(it)
    }
    onQueryChange(query)
    onQueryChanged += onQueryChange
}