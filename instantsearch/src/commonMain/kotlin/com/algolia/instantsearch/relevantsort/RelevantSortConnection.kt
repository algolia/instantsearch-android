@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.relevantsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.core.relevantsort.RelevantSortPresenter
import com.algolia.instantsearch.core.relevantsort.RelevantSortView
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.core.relevantsort.connectView
import com.algolia.instantsearch.migration2to3.Query
import com.algolia.instantsearch.relevantsort.internal.RelevantSortConnectionSearcherForHits
import com.algolia.instantsearch.searcher.SearcherForHits

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun RelevantSortViewModel.connectSearcher(searcher: SearcherForHits<Query>): Connection {
    return RelevantSortConnectionSearcherForHits(this, searcher)
}

/**
 * Connects a view to the relevant sort widget.
 *
 * @param view the view that will relevant sort state.
 * @param presenter defines the way we want to interact with a priority value
 */
public fun <T> RelevantSortConnector.connectView(
    view: RelevantSortView<T>,
    presenter: RelevantSortPresenter<T>
): Connection {
    return viewModel.connectView(view, presenter)
}
