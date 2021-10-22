package com.algolia.instantsearch.helper.searcher.multi.hits

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.helper.searcher.multi.hits.internal.HitsSearcherImpl
import com.algolia.instantsearch.helper.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for hits.
 */
public interface HitsSearcher : Searcher<ResponseSearch> {
    public var filterGroups: Set<FilterGroup<*>> // TODO() make it internal, modifiable with filter state connections
}

/**
 * Creates an instance of [HitsSearcher].
 */
public fun HitsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
): HitsSearcher = HitsSearcherImpl(
    client = client,
    indexedQuery = IndexQuery(indexName, query),
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
)

/**
 * Adds a [HitsSearcher] to the [MultiSearcher] instance.
 */
public fun MultiSearcher.addHitsSearcher(
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null
): HitsSearcher {
    return HitsSearcherImpl(
        client = client,
        indexedQuery = IndexQuery(indexName, query),
        requestOptions = requestOptions,
        coroutineScope = coroutineScope,
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
