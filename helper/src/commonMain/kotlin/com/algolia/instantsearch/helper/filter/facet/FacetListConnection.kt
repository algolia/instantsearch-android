@file:JvmName("FacetList")

package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this FacetListViewModel to a FilterState,
 * updating it according to [selectionMode][FacetListViewModel.selectionMode] when the selection changes
 * and updating the viewModel's data when the filterState changes.
 *
 * @param groupID a FilterGroupID to group related filters.
 */
//FIXME: Overload resolution ambiguity -> move connectors to separate packages
public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or)
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, groupID)
}

/**
 * Connects this FacetListViewModel to a FilterState,
 * updating it according to [selectionMode][FacetListViewModel.selectionMode] when the selection changes
 * and updating the viewModel's data when the filterState changes.
 *
 * @param operator an operator to group related [attribute] filters.
 */
@JvmName("connectFilterStateWithOperator")
public fun FacetListViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: FilterOperator = FilterOperator.Or
): Connection {
    return FacetListConnectionFilterState(this, filterState, attribute, FilterGroupID(attribute, operator))
}

/**
 * Connects this FacetListViewModel to a Searcher,
 * adding [attribute] to its [faceted attributes][com.algolia.search.model.search.Query.facets]
 * and updating the viewModel when the facets change.
 */
public fun FacetListViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    attribute: Attribute
): Connection {
    return FacetListConnectionSearcher(this, searcher, attribute)
}

/**
 * Connects this FacetListViewModel to a SearcherForFacets, updating it on new facets.
 */
public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets
): Connection {
    return FacetListConnectionSearcherForFacets(this, searcher)
}

/**
 * Connects this FacetListViewModel to a FacetListView, updating it when facets change.
 */
@JvmOverloads
public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return FacetListConnectionView(this, view, presenter)
}

/**
 * Connects this FacetListConnector to a FacetListView, updating it when facets change.
 */
@JvmOverloads
public fun FacetListConnector.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
): Connection {
    return viewModel.connectView(view, presenter)
}