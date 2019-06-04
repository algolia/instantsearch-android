package com.algolia.instantsearch.core.searchbox


public fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setItem(item)
    searchBoxView.onQueryChanged = { item = it }
    searchBoxView.onQuerySubmitted = {
        item = it
        submitQuery()
    }
}
