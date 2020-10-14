package com.algolia.instantsearch.helper.customdata

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.customdata.internal.QueryRuleCustomDataConnectionMultipleIndex
import com.algolia.instantsearch.helper.customdata.internal.QueryRuleCustomDataConnectionSingleIndex
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun <T> QueryRuleCustomDataViewModel<T>.connectSearcher(searcher: SearcherSingleIndex): Connection {
    return QueryRuleCustomDataConnectionSingleIndex(this, searcher)
}

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 * @param queryIndex index of query from response of which the user data will be extracted
 */
public fun <T> QueryRuleCustomDataViewModel<T>.connectSearcher(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
): Connection {
    return QueryRuleCustomDataConnectionMultipleIndex(this, searcher, queryIndex)
}
