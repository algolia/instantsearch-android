package com.algolia.instantsearch.compose.filter.facet.internal

import com.algolia.instantsearch.compose.paging.Paginator
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.search.model.search.Facet

/**
 * Connection implementation between [FacetListViewModel] and [Paginator].
 *
 * @param facetListViewModel facet list view model handling selections
 * @param paginator component handling Paged data
 */
internal class FacetListConnectionPager<T : Any>(
    private val facetListViewModel: FacetListViewModel,
    private val paginator: Paginator<T>
) : ConnectionImpl() {

    private val facetsSubscription: (List<Pair<Facet, Boolean>>) -> Unit = {
        paginator.invalidate()
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
