package com.algolia.instantsearch.searcher.composition

import com.algolia.client.api.CompositionClient
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.SearcherForFacets
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.composition.internal.DefaultCompositionFacetsSearchService
import com.algolia.instantsearch.searcher.composition.internal.DefaultCompositionFacetsSearcher
import com.algolia.instantsearch.searcher.facets.SearchForFacetQuery
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values using the Algolia Composition API
 * (`/1/compositions/{compositionID}/facets/{facetName}/query` endpoint).
 */
public interface CompositionFacetsSearcher : SearcherForFacets<SearchParamsObject> {

    /**
     * Unique Composition ObjectID.
     */
    public var compositionID: String

    /**
     * Facets query.
     */
    public var facetQuery: String?

    /**
     * Maximum number of facet values to return.
     */
    public var maxFacetHits: Int?
}

/**
 * Creates an instance of [CompositionFacetsSearcher].
 *
 * @param client composition client instance
 * @param compositionID unique Composition ObjectID
 * @param attribute facet attribute
 * @param query the query used to narrow down the facet values search
 * @param facetQuery the facet query used to search for facets
 * @param maxFacetHits maximum number of facet values to return
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun CompositionFacetsSearcher(
    client: CompositionClient,
    compositionID: String,
    attribute: String,
    query: SearchParamsObject = SearchParamsObject(),
    facetQuery: String? = null,
    maxFacetHits: Int? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForFacetQuery = SearchForFacetQuery.All,
): CompositionFacetsSearcher = DefaultCompositionFacetsSearcher(
    searchService = DefaultCompositionFacetsSearchService(client),
    compositionID = compositionID,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    maxFacetHits = maxFacetHits,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)

/**
 * Creates an instance of [CompositionFacetsSearcher].
 *
 * @param applicationID application ID
 * @param apiKey API Key
 * @param compositionID unique Composition ObjectID
 * @param attribute facet attribute
 * @param query the query used to narrow down the facet values search
 * @param facetQuery the facet query used to search for facets
 * @param maxFacetHits maximum number of facet values to return
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun CompositionFacetsSearcher(
    applicationID: String,
    apiKey: String,
    compositionID: String,
    attribute: String,
    query: SearchParamsObject = SearchParamsObject(),
    facetQuery: String? = null,
    maxFacetHits: Int? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForFacetQuery = SearchForFacetQuery.All,
): CompositionFacetsSearcher = CompositionFacetsSearcher(
    client = CompositionClient(applicationID, apiKey),
    compositionID = compositionID,
    attribute = attribute,
    query = query,
    facetQuery = facetQuery,
    maxFacetHits = maxFacetHits,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)
