package com.algolia.instantsearch.helper.smartsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.smartsort.SmartSortConnector
import com.algolia.instantsearch.core.smartsort.SmartSortPresenter
import com.algolia.instantsearch.core.smartsort.SmartSortView
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.core.smartsort.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectionMultipleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectionSingleIndex

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun SmartSortViewModel.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return SmartSortConnectionSingleIndex(this, searcher)
}

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 * @param queryIndex query index
 */
public fun SmartSortViewModel.connectSearcher(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
): Connection {
    return SmartSortConnectionMultipleIndex(this, searcher, queryIndex)
}

/**
 * Connects a view to the Smart sort widget.
 *
 * @param view the view that will smart sort state.
 * @param presenter defines the way we want to interact with a priority value
 */
public fun SmartSortConnector.connectView(view: SmartSortView, presenter: SmartSortPresenter? = null): Connection {
    return viewModel.connectView(view, presenter)
}
