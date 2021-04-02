package com.algolia.instantsearch.compose.filter

import com.algolia.instantsearch.compose.filter.internal.FacetListConnectionPager
import com.algolia.instantsearch.compose.paging.SearcherPager
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel

public fun <T : Any> FacetListConnector.connectSearcherPager(pager: SearcherPager<T>): Connection {
    return viewModel.connectSearcherPager(pager)
}

public fun <T : Any> FacetListViewModel.connectSearcherPager(pager: SearcherPager<T>): Connection {
    return FacetListConnectionPager(this, pager)
}
