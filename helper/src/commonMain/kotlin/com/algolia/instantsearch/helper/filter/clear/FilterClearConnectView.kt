package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection


public fun FilterClearViewModel.connectionView(
    view: FilterClearView
): Connection {
    return FilterClearConnectionView(this, view)
}