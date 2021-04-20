package com.algolia.instantsearch.compose.filter.internal

import com.algolia.instantsearch.compose.paging.SearcherPager
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.search.model.search.Facet

/**
 * Connection implementation between [FacetListViewModel] and [SearcherPager].
 */
internal class FacetListConnectionPager<T : Any>(
    private val facetListViewModel: FacetListViewModel,
    private val pager: SearcherPager<T>
) : ConnectionImpl() {

    private val callback: (List<Pair<Facet, Boolean>>) -> Unit = {
        pager.notifySearcherChanged()
    }

    override fun connect() {
        super.connect()
        facetListViewModel.facets.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        facetListViewModel.facets.unsubscribe(callback)
    }
}
