package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl(),
    connect: Boolean = true
): Connection {
    return FilterCurrentConnectionView(this, view, presenter).autoConnect(connect)
}