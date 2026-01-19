package com.algolia.instantsearch.relateditems

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.relateditems.internal.RelatedItemsConnectionView
import com.algolia.instantsearch.searcher.SearcherForHits

/**
 * Connects [Searcher] to [HitsView] to display related items.
 *
 * @param adapter hits views adapter
 * @param hit hit to get its related items
 * @param matchingPatterns list of matching patterns that create scored filters based on the hit’s attributes
 * @param presenter presentation output and format
 */
public fun <T : Indexable> SearcherForHits<*>.connectRelatedHitsView(
    adapter: HitsView<T>,
    hit: T,
    matchingPatterns: List<MatchingPattern<T>>,
    presenter: Presenter<SearchResponse, List<T>>,
): Connection {
    return RelatedItemsConnectionView(this, adapter, hit, matchingPatterns, presenter)
}
