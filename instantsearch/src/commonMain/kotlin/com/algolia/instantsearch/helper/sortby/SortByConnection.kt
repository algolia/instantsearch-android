package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.index.DefaultIndexPresenter
import com.algolia.instantsearch.helper.index.IndexNamePresenter
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.internal.SortByConnectionSearcher

/**
 * Connects a view to the SortBy widget.
 */
public fun <S> SortByConnector<S>.connectView(
    view: SortByView,
    presenter: IndexNamePresenter = DefaultIndexPresenter,
): Connection where S : Searcher<*>, S : IndexNameHolder {
    return viewModel.connectView(view, presenter)
}

/**
 * Connects a view to the SortBy view model.
 */
public fun SortByViewModel.connectView(
    view: SortByView,
    presenter: IndexNamePresenter = DefaultIndexPresenter,
): Connection {
    return connectView(view, presenter)
}

/**
 * Connects a searcher to the SortBy view model.
 */
public fun <S> SortByViewModel.connectSearcher(
    searcher: S,
): Connection where S : Searcher<*>, S : IndexNameHolder {
    return SortByConnectionSearcher(this, searcher)
}
