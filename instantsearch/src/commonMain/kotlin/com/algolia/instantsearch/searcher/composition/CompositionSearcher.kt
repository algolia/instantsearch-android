package com.algolia.instantsearch.searcher.composition

import com.algolia.client.api.CompositionClient
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.FilterGroupsHolder
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.composition.internal.DefaultCompositionSearchService
import com.algolia.instantsearch.searcher.composition.internal.DefaultCompositionSearcher
import com.algolia.instantsearch.searcher.hits.SearchForQuery
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation runs an Algolia Composition (`/1/compositions/{compositionID}/run` endpoint).
 *
 * The composition run response is presented as a regular [com.algolia.client.model.search.SearchResponse],
 * so a [CompositionSearcher] can be connected to the same components as a
 * [com.algolia.instantsearch.searcher.hits.HitsSearcher] (hits, stats, facet list, search box,
 * filter state, paging, ...).
 *
 * Unlike [com.algolia.instantsearch.searcher.hits.HitsSearcher], disjunctive faceting is handled
 * server-side by the Composition API: the refined disjunctive attributes are annotated with the
 * `disjunctive` modifier in the requested facets instead of performing a multi-query fan-out.
 * A [CompositionSearcher] cannot join a [com.algolia.instantsearch.searcher.multi.MultiSearcher].
 */
public interface CompositionSearcher : SearcherForHits<SearchParamsObject>, FilterGroupsHolder {

    /**
     * Unique Composition ObjectID.
     */
    public var compositionID: String
}

/**
 * Creates an instance of [CompositionSearcher].
 *
 * @param client composition client instance
 * @param compositionID unique Composition ObjectID
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun CompositionSearcher(
    client: CompositionClient,
    compositionID: String,
    query: SearchParamsObject = SearchParamsObject(),
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForQuery = SearchForQuery.All,
): CompositionSearcher = DefaultCompositionSearcher(
    searchService = DefaultCompositionSearchService(client),
    compositionID = compositionID,
    query = query,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)

/**
 * Creates an instance of [CompositionSearcher].
 *
 * @param applicationID application ID
 * @param apiKey API Key
 * @param compositionID unique Composition ObjectID
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun CompositionSearcher(
    applicationID: String,
    apiKey: String,
    compositionID: String,
    query: SearchParamsObject = SearchParamsObject(),
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForQuery = SearchForQuery.All,
): CompositionSearcher = CompositionSearcher(
    client = CompositionClient(applicationID, apiKey),
    compositionID = compositionID,
    query = query,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)
