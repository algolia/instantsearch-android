package com.algolia.instantsearch.stats

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.stats.internal.StatsConnectionSearcher
import com.algolia.instantsearch.stats.internal.StatsConnectionView
import com.algolia.search.model.response.ResponseSearch
import kotlin.jvm.JvmName

@JvmName("connectViewString")
public fun StatsViewModel.connectView(
    view: StatsView<String>,
    presenter: StatsPresenter<String> = StatsPresenterImpl(),
): Connection {
    return StatsConnectionView(this, view, presenter)
}

public fun <T> StatsViewModel.connectView(
    view: StatsView<T>,
    presenter: StatsPresenter<T>,
): Connection {
    return StatsConnectionView(this, view, presenter)
}

public fun StatsViewModel.connectSearcher(
    searcher: Searcher<ResponseSearch>,
): Connection {
    return StatsConnectionSearcher(this, searcher)
}

/**
 * Connects a view to the Stats widget.
 *
 * @param view the view that will render the facets
 * @param presenter controls the sorting and other settings of the facet list view
 */
public fun <T> StatsConnector.connectView(
    view: StatsView<T>,
    presenter: StatsPresenter<T>,
): Connection {
    return viewModel.connectView(view, presenter)
}
