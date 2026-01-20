package com.algolia.instantsearch.android.paging3.facet.internal

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.filter.facet.FacetListViewModel

/**
 * Connection implementation between [FacetListViewModel] and [Paginator].
 *
 * @param facetListViewModel facet list view model handling selections
 * @param paginator component handling Paged data
 */
internal class FacetListConnectionPager<T : Any>(
    private val facetListViewModel: FacetListViewModel,
    private val paginator: Paginator<T>
) : AbstractConnection() {

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
