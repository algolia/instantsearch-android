@file:JvmName("Hierarchical")

package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.tree.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import kotlin.jvm.JvmName

/**
 * Connects this HierarchicalViewModel to a FilterState, updating its filters on new selection
 * and updating the viewModel's data when the filterState changes.
 */
public fun HierarchicalViewModel.connectFilterState(filterState: FilterState): Connection {
    return HierarchicalConnectionFilterState(this, filterState)
}

/**
 * Connects this HierarchicalViewModel to a SearcherSingleIndex,
 * adding its [attribute][HierarchicalViewModel.attribute] to the Searcher's [faceted attributes][com.algolia.search.model.search.Query.facets]
 * and updating the viewModel's data when the facets change.
 */
public fun HierarchicalViewModel.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return HierarchicalConnectionSearcher(this, searcher)
}

/**
 * Conencts this HierarchicalConnector to a HierarchicalView,
 * updating it when the viewModel's data changes.
 */
public fun HierarchicalConnector.connectView(
    view: HierarchicalView,
    presenter: HierarchicalPresenter<List<HierarchicalItem>>
): Connection {
    return viewModel.connectView(view, presenter)
}