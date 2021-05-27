package com.algolia.instantsearch.compose.filter.internal

import com.algolia.instantsearch.compose.paging.SearcherPager
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.search.model.search.Facet

/**
 * Connection implementation between [FacetListViewModel] and [SearcherPager].
 *
 * @param facetListViewModel facet list view model handling selections
 * @param searchPager search pages generation logic handler
 */
internal class FacetListConnectionPager<T : Any>(
    private val facetListViewModel: FacetListViewModel,
    private val searchPager: SearcherPager<T>
) : ConnectionImpl() {

    private val facetsSubscription: (List<Pair<Facet, Boolean>>) -> Unit = {
        searchPager.notifySearcherChanged()
    }

    override fun connect() {
        super.connect()
        facetListViewModel.facets.subscribe(facetsSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        facetListViewModel.facets.unsubscribe(facetsSubscription)
    }
}
