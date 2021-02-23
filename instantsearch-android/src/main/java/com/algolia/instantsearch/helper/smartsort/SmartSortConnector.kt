package com.algolia.instantsearch.helper.smartsort

import com.algolia.instantsearch.core.smartsort.SmartSortConnector
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectorMultipleIndex
import com.algolia.instantsearch.helper.smartsort.internal.SmartSortConnectorSingleIndex

/**
 * Creates a [SmartSortConnector] instance.
 *
 * @param searcher searcher that handles your searches
 * @param viewModel component handling Smart sort logic
 */
public fun SmartSortConnector(
    searcher: SearcherSingleIndex,
    viewModel: SmartSortViewModel = SmartSortViewModel(),
): SmartSortConnector {
    return SmartSortConnectorSingleIndex(searcher, viewModel)
}

/**
 * Creates a [SmartSortConnector] instance.
 *
 * @param searcher searcher that handles your searches
 * @param queryIndex index of query from response of which the user data will be extracted
 * @param viewModel component handling Smart sort logic
 */
public fun SmartSortConnector(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
    viewModel: SmartSortViewModel = SmartSortViewModel(),
): SmartSortConnector {
    return SmartSortConnectorMultipleIndex(searcher, queryIndex, viewModel)
}
