package com.algolia.instantsearch.helper.relevantsort.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.helper.extension.traceRelevantSortConnector
import com.algolia.instantsearch.helper.relevantsort.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex

@Deprecated("Use Searchers aggregated with MultiSearcher instead")
internal class RelevantSortConnectorMultipleIndex(
    override val searcher: SearcherMultipleIndex,
    queryIndex: Int,
    override val viewModel: RelevantSortViewModel = RelevantSortViewModel(),
) : ConnectionImpl(), RelevantSortConnector {

    private val connectionSearcher = viewModel.connectSearcher(searcher, queryIndex)

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
