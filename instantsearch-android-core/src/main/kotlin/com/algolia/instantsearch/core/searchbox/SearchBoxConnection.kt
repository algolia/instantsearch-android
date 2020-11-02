package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.internal.SearchBoxConnectionView

public fun SearchBoxViewModel.connectView(
    view: SearchBoxView
): Connection {
    return SearchBoxConnectionView(this, view)
}
