package com.algolia.instantsearch.searcher.hits

import com.algolia.instantsearch.searcher.FilterGroupsHolder
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.hits.internal.DefaultHitsSearchService
import com.algolia.instantsearch.searcher.hits.internal.DefaultHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for hits.
 */
public interface HitsSearcher : SearcherForHits<Query>, IndexNameHolder, FilterGroupsHolder {

    /**
     * Flag defining if disjunctive faceting is enabled.
     */
    public val isDisjunctiveFacetingEnabled: Boolean
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
public fun HitsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
    coroutineScope: CoroutineScope = SearcherScope(),
): HitsSearcher = DefaultHitsSearcher(
    searchService = DefaultHitsSearchService(client),
    indexName = indexName,
    query = query,
    requestOptions = requestOptions,
    isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
    coroutineScope = coroutineScope,
)

/**
 * Creates an instance of [HitsSearcher].
 *
 * @param applicationID application ID
 * @param apiKey API Key
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 */
public fun HitsSearcher(
    applicationID: ApplicationID,
    apiKey: APIKey,
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
    coroutineScope: CoroutineScope = SearcherScope(),
    triggerSearchForQuery: ((Query) -> Boolean)? = null
): HitsSearcher = DefaultHitsSearcher(
    searchService = DefaultHitsSearchService(client = ClientSearch(applicationID, apiKey)),
    indexName = indexName,
    query = query,
    requestOptions = requestOptions,
    isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
    coroutineScope = coroutineScope,
    triggerSearchForQuery = triggerSearchForQuery
)

/**
 * Adds a [HitsSearcher] to the [MultiSearcher] instance.
 *
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 */
public fun MultiSearcher.addHitsSearcher(
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
): HitsSearcher {
    return DefaultHitsSearcher(
        searchService = DefaultHitsSearchService(client),
        indexName = indexName,
        query = query,
        requestOptions = requestOptions,
        isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
        coroutineScope = coroutineScope,
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
