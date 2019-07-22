package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun IndexSegmentViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    connect: Boolean = true
): Connection {
    return IndexSegmentConnectionSearcher(this, searcher).autoConnect(connect)
}