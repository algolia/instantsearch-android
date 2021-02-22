package com.algolia.instantsearch.helper.smartsort.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.smartsort.SmartSortConnector
import com.algolia.instantsearch.helper.smartsort.connectSearcher

internal class SmartSortConnectorSingleIndex(
    searcher: SearcherSingleIndex,
    override val viewModel: SmartSortViewModel = SmartSortViewModel(),
) : ConnectionImpl(), SmartSortConnector {

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
