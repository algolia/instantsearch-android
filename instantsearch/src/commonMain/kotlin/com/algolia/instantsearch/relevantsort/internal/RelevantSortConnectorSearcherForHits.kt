package com.algolia.instantsearch.relevantsort.internal

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.extension.traceRelevantSortConnector
import com.algolia.instantsearch.migration2to3.Query
import com.algolia.instantsearch.relevantsort.connectSearcher
import com.algolia.instantsearch.searcher.SearcherForHits

internal class RelevantSortConnectorSearcherForHits(
    override val searcher: SearcherForHits<Query>,
    override val viewModel: RelevantSortViewModel = RelevantSortViewModel(),
) : AbstractConnection(), RelevantSortConnector {

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    init {
        traceRelevantSortConnector()
    }

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
