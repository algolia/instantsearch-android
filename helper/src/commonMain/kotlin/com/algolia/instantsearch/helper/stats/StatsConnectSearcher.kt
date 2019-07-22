package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun StatsViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    connect: Boolean = true
): Connection {
    return StatsConnectionSearcher(this, searcher).autoConnect(connect)
}