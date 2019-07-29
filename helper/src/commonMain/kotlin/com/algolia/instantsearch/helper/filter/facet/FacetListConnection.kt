package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, groupID)
}

public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: FilterOperator = FilterOperator.Or
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, FilterGroupID(attribute, operator))
}

public fun FacetListViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    attribute: Attribute
): Connection {
    return FacetListConnectionSearcher(this, searcher, attribute)
}

public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets
): Connection {
    return FacetListConnectionSearcherForFacets(this, searcher)
}

public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return FacetListConnectionView(this, view, presenter)
}

public fun FacetListConnector.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return viewModel.connectView(view, presenter)
}