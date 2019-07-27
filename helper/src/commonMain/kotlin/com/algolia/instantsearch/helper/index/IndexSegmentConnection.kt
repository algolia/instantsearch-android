package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectionView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun IndexSegmentViewModel.connectionView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return connectionView(view, presenter)
}

public fun IndexSegmentViewModel.connectionSearcher(
    searcher: SearcherSingleIndex
): Connection {
    return IndexSegmentConnectionSearcher(this, searcher)
}

public fun IndexSegmentWidget.connectionView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return viewModel.connectionView(view, presenter)
}