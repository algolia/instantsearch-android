package com.algolia.instantsearch.core.searchbox


interface SearchBoxView {

    fun setQuery(query: String?)

    var onQueryChanged: ((String?) -> Unit)?
    var onQuerySubmitted: ((String?) -> Unit)?
}