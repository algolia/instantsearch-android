package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchParamsObject

public fun SearchParamsObject.addFacet(vararg attribute: String): SearchParamsObject {
    val facets = (facets.orEmpty() + attribute).distinct()
    return copy(facets = facets)
}

public fun SearchParamsObject.removeFacet(attribute: String): SearchParamsObject {
    val facets = facets.orEmpty().filterNot { it == attribute }
    return copy(facets = facets)
}

public fun SearchParamsObject.updateQueryFacets(vararg attribute: String): SearchParamsObject {
    val facets = (facets.orEmpty() + attribute).distinct()
    return copy(facets = facets)
}

@Suppress("UNCHECKED_CAST")
public fun <R> SearcherQuery<*, R>.updateSearchParamsObject(
    transform: (SearchParamsObject) -> SearchParamsObject,
) {
    val params = query as? SearchParamsObject ?: return
    (this as? SearcherQuery<SearchParamsObject, R>)?.let { it.query = transform(params) }
}
