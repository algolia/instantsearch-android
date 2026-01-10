package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.search.SearchForFacetValuesResponse

@Deprecated("Replace with client mode", ReplaceWith("SearchForFacetValuesResponse"))
public typealias ResponseSearchForFacets = SearchForFacetValuesResponse
