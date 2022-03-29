@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.customdata

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.customdata.internal.QueryRuleCustomDataConnectionSearcherForHits
import com.algolia.instantsearch.searcher.SearcherForHits

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 */
public fun <T> QueryRuleCustomDataViewModel<T>.connectSearcher(searcher: SearcherForHits<*>): Connection {
    return QueryRuleCustomDataConnectionSearcherForHits(this, searcher)
}
