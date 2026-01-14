package com.algolia.instantsearch.searcher.facets

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.instantsearch.migration2to3.APIKey
import com.algolia.instantsearch.migration2to3.ApplicationID
import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.IndexName
import com.algolia.instantsearch.migration2to3.RequestOptions
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.searcher.SearcherForFacets
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.internal.DefaultFacetsSearchService
import com.algolia.instantsearch.searcher.facets.internal.DefaultFacetsSearcher
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.searcher.multi.internal.asMultiSearchComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
public interface FacetsSearcher : SearcherForFacets<SearchParamsObject>, IndexNameHolder {

    /**
     * Facets query.
     */
    public var facetQuery: String?
}

/**
 * Creates an instance of [FacetsSearcher].
 *
 * @param client search client instance
 * @param indexName index name
 * @param attribute facet attribute
 * @param query the query used for search
 * @param facetQuery the facet query used to search for facets
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun FacetsSearcher(
    client: SearchClient,
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForFacetQuery = SearchForFacetQuery.All,
): FacetsSearcher = DefaultFacetsSearcher(
    searchService = DefaultFacetsSearchService(client),
    indexName = indexName,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)

/**
 * Creates an instance of [FacetsSearcher].
 *
 * @param applicationID application ID
 * @param apiKey API Key
 * @param indexName index name
 * @param attribute facet attribute
 * @param query the query used for search
 * @param facetQuery the facet query used to search for facets
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 */
public fun FacetsSearcher(
    applicationID: ApplicationID,
    apiKey: APIKey,
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForFacetQuery = SearchForFacetQuery.All
): FacetsSearcher = DefaultFacetsSearcher(
    searchService = DefaultFacetsSearchService(client = SearchClient(applicationID, apiKey)),
    indexName = indexName,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
)

/**
 * Adds a [FacetsSearcher] to the [MultiSearcher] instance.
 *
 * @param indexName index name
 * @param attribute facet attribute
 * @param query the query used for search
 * @param facetQuery the facet query used to search for facets
 * @param requestOptions request local configuration
 * @param triggerSearchFor request condition
 */
public fun MultiSearcher.addFacetsSearcher(
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    triggerSearchFor: SearchForFacetQuery = SearchForFacetQuery.All
): FacetsSearcher {
    return DefaultFacetsSearcher(
        searchService = DefaultFacetsSearchService(client),
        indexName = indexName,
        query = query,
        attribute = attribute,
        facetQuery = facetQuery,
        requestOptions = requestOptions,
        coroutineScope = coroutineScope,
        coroutineDispatcher = coroutineDispatcher,
        triggerSearchFor = triggerSearchFor
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
