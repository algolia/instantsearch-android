package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
    connect: Boolean = true
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, groupID).autoConnect(connect)
}