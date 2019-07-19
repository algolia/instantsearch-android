package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection


public fun SearchBoxViewModel.connectView(
    view: SearchBoxView,
    connect: Boolean = true
): Connection {
    return SearchBoxConnectionView(this, view).apply { if (connect) connect() }
}