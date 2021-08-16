package com.algolia.instantsearch.helper.relevantsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.core.relevantsort.RelevantSortPresenter
import com.algolia.instantsearch.core.relevantsort.RelevantSortView
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.core.relevantsort.connectView
import com.algolia.instantsearch.helper.relevantsort.internal.RelevantSortConnectionMultipleIndex
import com.algolia.instantsearch.helper.relevantsort.internal.RelevantSortConnectionSingleIndex
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun RelevantSortViewModel.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return RelevantSortConnectionSingleIndex(this, searcher)
}

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 * @param queryIndex query index
 */
public fun RelevantSortViewModel.connectSearcher(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
): Connection {
    return RelevantSortConnectionMultipleIndex(this, searcher, queryIndex)
}

/**
 * Connects a view to the relevant sort widget.
 *
 * @param view the view that will relevant sort state.
 * @param presenter defines the way we want to interact with a priority value
 */
public fun <T> RelevantSortConnector.connectView(view: RelevantSortView<T>, presenter: RelevantSortPresenter<T>): Connection {
    return viewModel.connectView(view, presenter)
}
