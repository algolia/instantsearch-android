package com.algolia.instantsearch.helper.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.dynamic.DynamicSortViewModel
import com.algolia.instantsearch.helper.dynamic.internal.DynamicSortToggleConnectionMultipleIndex
import com.algolia.instantsearch.helper.dynamic.internal.DynamicSortToggleConnectionSingleIndex
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun DynamicSortViewModel.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return DynamicSortToggleConnectionSingleIndex(this, searcher)
}

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 * @param queryIndex query index
 */
public fun DynamicSortViewModel.connectSearcher(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
): Connection {
    return DynamicSortToggleConnectionMultipleIndex(this, searcher, queryIndex)
}
