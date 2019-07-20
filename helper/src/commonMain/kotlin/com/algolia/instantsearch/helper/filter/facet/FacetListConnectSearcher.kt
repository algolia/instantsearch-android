package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    attribute: Attribute,
    connect: Boolean = true
): Connection {
    return FacetListConnectionSearcher(this, searcher, attribute).autoConnect(connect)
}

public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets,
    connect: Boolean = true
): Connection {
    return FacetListConnectionSearcherForFacets(this, searcher).autoConnect(connect)
}