package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun IndexSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return connectView(view, presenter)
}

public fun IndexSegmentViewModel.connectSearcher(
    searcher: SearcherSingleIndex
): Connection {
    return IndexSegmentConnectionSearcher(this, searcher)
}

public fun IndexSegmentWidget.connectView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return viewModel.connectView(view, presenter)
}