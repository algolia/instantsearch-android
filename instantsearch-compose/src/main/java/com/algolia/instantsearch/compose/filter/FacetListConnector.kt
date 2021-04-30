package com.algolia.instantsearch.compose.filter

import com.algolia.instantsearch.compose.filter.internal.FacetListConnectionPager
import com.algolia.instantsearch.compose.paging.SearcherPager
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel

/**
 * Create a connection between a searcher pager and the facet list components.
 *
 * @param searcherPager pager to be connected
 */
public fun <T : Any> FacetListConnector.connectSearcherPager(searcherPager: SearcherPager<T>): Connection {
    return viewModel.connectSearcherPager(searcherPager)
}

/**
 * Create a connection between a searcher pager and the facet list view model.
 *
 * @param searcherPager pager to be connected
 */
public fun <T : Any> FacetListViewModel.connectSearcherPager(searcherPager: SearcherPager<T>): Connection {
    return FacetListConnectionPager(this, searcherPager)
}
