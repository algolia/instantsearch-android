package com.algolia.instantsearch.core.searchbox


public interface SearchBoxView {

    public fun setQuery(query: String?)

    public var onQueryChanged: ((String?) -> Unit)?
    public var onQuerySubmitted: ((String?) -> Unit)?
}