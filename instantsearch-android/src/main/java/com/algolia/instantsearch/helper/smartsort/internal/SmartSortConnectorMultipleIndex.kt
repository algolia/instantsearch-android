package com.algolia.instantsearch.helper.smartsort.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.smartsort.SmartSortConnector
import com.algolia.instantsearch.helper.smartsort.connectSearcher

internal class SmartSortConnectorMultipleIndex(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
    override val viewModel: SmartSortViewModel = SmartSortViewModel(),
) : ConnectionImpl(), SmartSortConnector {

    private val connectionSearcher = viewModel.connectSearcher(searcher, queryIndex)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
