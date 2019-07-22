package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <T> StatsViewModel.connectView(
    view: StatsView<T>,
    connect: Boolean = true,
    presenter: StatsPresenter<T>
): Connection {
    return StatsConnectionView(this, view, presenter).autoConnect(connect)
}