package com.algolia.instantsearch.compose.filter.facet.internal

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem

/**
 * [FacetListState] implementation.
 *
 * @param items initial facets list value
 */
internal class FacetListStateImpl(
    items: List<SelectableItem<FacetHits>>
) : FacetListState, SelectableListState<FacetHits> by SelectableListState(items) {

    init {
        trace()
    }
}
