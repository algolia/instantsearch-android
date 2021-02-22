package com.algolia.instantsearch.helper.smartsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectorMultipleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectorSingleIndex

/**
 * TODO
 */
public interface SmartSortConnector : Connection {

    public val viewModel: SmartSortViewModel
}

/**
 * TODO
 */
public fun SmartSortConnector(
    searcher: SearcherSingleIndex,
    viewModel: SmartSortViewModel = SmartSortViewModel(),
): SmartSortConnector {
    return SmartSortConnectorSingleIndex(searcher, viewModel)
}

/**
 * TODO
 */
public fun SmartSortConnector(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
    viewModel: SmartSortViewModel = SmartSortViewModel(),
): SmartSortConnector {
    return SmartSortConnectorMultipleIndex(searcher, queryIndex, viewModel)
}
