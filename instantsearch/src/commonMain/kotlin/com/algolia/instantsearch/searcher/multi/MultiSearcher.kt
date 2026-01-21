package com.algolia.instantsearch.searcher.multi

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchResult
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searcher.multi.internal.types.IndexedQuery
import com.algolia.instantsearch.searcher.multi.internal.types.MultipleQueriesStrategy
import com.algolia.instantsearch.searcher.multi.internal.types.ResponseMultiSearch
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.multi.internal.DefaultMultiSearchService
import com.algolia.instantsearch.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 *  Extracts queries from queries sources, performs search request and dispatches the results to the corresponding receivers.
 */
public abstract class MultiSearcher : Searcher<ResponseMultiSearch> {

    /**
     * Client to perform operations on indices.
     */
    internal abstract val client: SearchClient

    /**
     * Adds a search component to the multi-searcher.
     */
    internal abstract fun addSearcher(component: MultiSearchComponent<IndexedQuery, SearchResult>)
}

/**
 * Creates an instance of [MultiSearcher].
 *
 * @param client search client instance
 * @param strategy multi-queries search strategy
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 */
public fun MultiSearcher(
    client: SearchClient,
    strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
): MultiSearcher = DefaultMultiSearcher(
    searchService = DefaultMultiSearchService(client),
    strategy = strategy,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
)

/**
 * Creates an instance of [MultiSearcher].
 *
 * @param appId application ID
 * @param apiKey API Key
 * @param strategy multi-queries search strategy
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 */
public fun MultiSearcher(
    appId: String,
    apiKey: String,
    strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
): MultiSearcher = DefaultMultiSearcher(
    searchService = DefaultMultiSearchService(client = SearchClient(appId, apiKey)),
    strategy = strategy,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
)
