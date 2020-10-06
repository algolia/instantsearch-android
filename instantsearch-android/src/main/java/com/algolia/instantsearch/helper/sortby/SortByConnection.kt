package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.index.IndexPresenter
import com.algolia.instantsearch.helper.index.IndexPresenterImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.sortby.internal.SortByConnectionSearcher

public fun SortByViewModel.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl,
): Connection {
    return connectView(view, presenter)
}

public fun SortByViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
): Connection {
    return SortByConnectionSearcher(this, searcher)
}

/**
 * Connects a view to the SortBy widget.
 *
 * @param view the view that will render the list of indices
 * @param presenter defines the way we want to display an index, taking as input an Index and returning a String.
 */
public fun SortByConnector.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl,
): Connection {
    return viewModel.connectView(view, presenter)
}
