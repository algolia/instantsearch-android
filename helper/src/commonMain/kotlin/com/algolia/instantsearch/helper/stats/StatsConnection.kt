package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun StatsViewModel.connectView(
    view: StatsView<String>,
    presenter: StatsPresenter<String> = StatsPresenterImpl()
) : Connection {
    return StatsConnectionView<String>(this, view, presenter)
}

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

public fun <T> StatsConnector.connectView(
    view: StatsView<T>,
    presenter: StatsPresenter<T>
): Connection {
    return viewModel.connectView(view, presenter)
}