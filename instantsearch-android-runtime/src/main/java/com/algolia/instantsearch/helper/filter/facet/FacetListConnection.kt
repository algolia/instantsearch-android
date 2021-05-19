package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.internal.FacetListConnectionFilterState
import com.algolia.instantsearch.helper.filter.facet.internal.FacetListConnectionSearcher
import com.algolia.instantsearch.helper.filter.facet.internal.FacetListConnectionSearcherForFacets
import com.algolia.instantsearch.helper.filter.facet.internal.FacetListConnectionView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.Attribute

public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, groupID)
}

public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: FilterOperator = FilterOperator.Or,
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, FilterGroupID(attribute, operator))
}

public fun FacetListViewModel.connectSearcher(
    searcher: SearcherIndex<*>,
    attribute: Attribute,
): Connection {
    return FacetListConnectionSearcher(this, searcher, attribute)
}

public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets,
): Connection {
    return FacetListConnectionSearcherForFacets(this, searcher)
}

public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null,
): Connection {
    return FacetListConnectionView(this, view, presenter)
}

/**
 * Create a connection between a view to the refinement list components.
 *
 * @param view
 * @param presenter
 */
public fun FacetListConnector.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null,
): Connection {
    return viewModel.connectView(view, presenter)
}
