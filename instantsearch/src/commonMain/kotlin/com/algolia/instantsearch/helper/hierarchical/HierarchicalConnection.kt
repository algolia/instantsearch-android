package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.tree.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.hierarchical.internal.HierarchicalConnectionFilterState
import com.algolia.instantsearch.helper.hierarchical.internal.HierarchicalConnectionSearcher
import com.algolia.instantsearch.helper.searcher.util.SearcherForHits

public fun HierarchicalViewModel.connectFilterState(filterState: FilterState): Connection {
    return HierarchicalConnectionFilterState(this, filterState)
}

public fun HierarchicalViewModel.connectSearcher(searcher: SearcherForHits<*>): Connection {
    return HierarchicalConnectionSearcher(this, searcher)
}

/**
 * Create a connection between a view and the hierarchical menu components
 *
 * @param view the view that will render the hierarchical menu
 * @param presenter defines the way we want to display the list of [HierarchicalItem]. A HierarchicalItem contains
 * a Facet, its level and a displayName.
 */
public fun HierarchicalConnector.connectView(
    view: HierarchicalView,
    presenter: HierarchicalPresenter<List<HierarchicalItem>>,
): Connection {
    return viewModel.connectView(view, presenter)
}
