package com.algolia.instantsearch.core.searchbox


public fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setItem(query.get())
    searchBoxView.onQueryChanged = (query::set)
    searchBoxView.onQuerySubmitted = {
        query.set(it)
        event.send(it)
    }
}