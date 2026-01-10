package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.search.SearchParamsObject

@Deprecated(
    message = "Replace with client mode",
    ReplaceWith("SearchParamsObject"),
    level = DeprecationLevel.ERROR,
)
public typealias Query = SearchParamsObject
