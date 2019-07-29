package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.index.*
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun SortByViewModel.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return connectView(view, presenter)
}

public fun SortByViewModel.connectSearcher(
    searcher: SearcherSingleIndex
): Connection {
    return SortByConnectionSearcher(this, searcher)
}

public fun SortByConnector.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return viewModel.connectView(view, presenter)
}