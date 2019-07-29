package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun <T> StatsViewModel.connectView(
    view: StatsView<T>,
    presenter: StatsPresenter<T>
): Connection {
    return StatsConnectionView(this, view, presenter)
}

public fun StatsViewModel.connectSearcher(
    searcher: SearcherSingleIndex
): Connection {
    return StatsConnectionSearcher(this, searcher)
}

public fun <T> StatsWidget.connectView(
    view: StatsView<T>,
    presenter: StatsPresenter<T>
): Connection {
    return viewModel.connectView(view, presenter)
}