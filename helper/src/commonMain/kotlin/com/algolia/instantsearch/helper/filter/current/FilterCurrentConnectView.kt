package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection


public fun FilterCurrentViewModel.connectionView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter
): Connection {
    return FilterCurrentConnectionView(this, view, presenter)
}