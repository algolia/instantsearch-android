package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.search.SearchResult

@Deprecated("Replace with client code", ReplaceWith("SearchResult"))
public typealias ResultSearch = SearchResult

