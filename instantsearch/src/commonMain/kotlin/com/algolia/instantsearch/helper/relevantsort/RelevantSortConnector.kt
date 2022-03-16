@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.helper.relevantsort

import com.algolia.instantsearch.core.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.helper.relevantsort.internal.RelevantSortConnectorSearcherForHits
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import com.algolia.search.model.search.Query

/**
 * Creates a [RelevantSortConnector] instance.
 *
 * @param searcher searcher that handles your searches
 * @param viewModel component handling relevant sort logic
 */
public fun RelevantSortConnector(
    searcher: SearcherForHits<Query>,
    viewModel: RelevantSortViewModel = RelevantSortViewModel(),
): RelevantSortConnector {
    return RelevantSortConnectorSearcherForHits(searcher, viewModel)
}
