package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.tree.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun HierarchicalViewModel.connectFilterState(filterState: FilterState): Connection {
    return HierarchicalConnectionFilterState(this, filterState)
}

public fun HierarchicalViewModel.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return HierarchicalConnectionSearcher(this, searcher)
}

public fun HierarchicalConnector.connectView(
    view: HierarchicalView,
    presenter: HierarchicalPresenter<List<HierarchicalItem>>
): Connection {
    return viewModel.connectView(view, presenter)
}