@file:Suppress("FunctionName")

package com.algolia.search.model.search

import com.algolia.client.model.search.SearchParamsObject

@Deprecated("Legacy query. Use SearchParamsObject.")
public typealias Query = SearchParamsObject

@Deprecated("Legacy query factory. Use SearchParamsObject directly.")
public fun Query(
    hitsPerPage: Int? = null,
    maxFacetHits: Int? = null,
): SearchParamsObject = SearchParamsObject(
    hitsPerPage = hitsPerPage,
)
