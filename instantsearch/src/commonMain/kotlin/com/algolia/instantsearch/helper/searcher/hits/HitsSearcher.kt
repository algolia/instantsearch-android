package com.algolia.instantsearch.helper.searcher.hits

import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.helper.searcher.hits.internal.DefaultHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for hits.
 */
@ExperimentalInstantSearch
public interface HitsSearcher : Searcher<ResponseSearch> {

    /**
     * Index name for search operations
     */
    public var indexName: IndexName

    /**
     * Query to run.
     */
    public val query: Query

    /**
     * Additional/Custom request options.
     */
    public val requestOptions: RequestOptions?
}

/**
 * Creates an instance of [HitsSearcher].
 *
 * @param client search client instance
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 */
@ExperimentalInstantSearch
public fun HitsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
): HitsSearcher = DefaultHitsSearcher(
    client = client,
    indexName = indexName,
    query = query,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
)

/**
 * Adds a [HitsSearcher] to the [MultiSearcher] instance.
 *
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 */
@ExperimentalInstantSearch
public fun MultiSearcher.addHitsSearcher(
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null
): HitsSearcher {
    return DefaultHitsSearcher(
        client = client,
        indexName = indexName,
        query = query,
        requestOptions = requestOptions,
        coroutineScope = coroutineScope,
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
