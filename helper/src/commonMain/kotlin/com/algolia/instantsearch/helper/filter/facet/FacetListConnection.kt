package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectionFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or)
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, groupID)
}

public fun FacetListViewModel.connectionSearcher(
    searcher: SearcherSingleIndex,
    attribute: Attribute
): Connection {
    return FacetListConnectionSearcher(this, searcher, attribute)
}

public fun FacetListViewModel.connectionSearcherForFacet(
    searcher: SearcherForFacets
): Connection {
    return FacetListConnectionSearcherForFacets(this, searcher)
}

public fun FacetListViewModel.connectionView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return FacetListConnectionView(this, view, presenter)
}

public fun FacetListWidget.connectionView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return viewModel.connectionView(view, presenter)
}