package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionFilterState
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionSearcherIndex
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherIndex

public fun DynamicFacetViewModel.connectSearcher(searcher: SearcherIndex<*>): Connection {
    return DynamicFacetConnectionSearcherIndex(this, searcher)
}

public fun DynamicFacetViewModel.connectFilterState(filterState: FilterState): Connection {
    return DynamicFacetConnectionFilterState(this, filterState)
}

public fun DynamicFacetViewModel.connectView(view: DynamicFacetView): Connection {
    return DynamicFacetConnectionView(this, view)
}
