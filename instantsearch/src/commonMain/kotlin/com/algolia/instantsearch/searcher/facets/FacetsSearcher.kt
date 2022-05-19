package com.algolia.instantsearch.searcher.facets

import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.searcher.SearcherForFacets
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.internal.DefaultFacetsSearchService
import com.algolia.instantsearch.searcher.facets.internal.DefaultFacetsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
public interface FacetsSearcher : SearcherForFacets<Query>, IndexNameHolder {

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
 */
public fun FacetsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
    triggerSearchFor: SearchForFacetQuery? = null,
): FacetsSearcher = DefaultFacetsSearcher(
    searchService = DefaultFacetsSearchService(client),
    indexName = indexName,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
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
    triggerSearchFor: SearchForFacetQuery? = null
): FacetsSearcher = DefaultFacetsSearcher(
    searchService = DefaultFacetsSearchService(client = ClientSearch(applicationID, apiKey)),
    indexName = indexName,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
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
 */
public fun MultiSearcher.addFacetsSearcher(
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    triggerSearchFor: SearchForFacetQuery? = null
): FacetsSearcher {
    return DefaultFacetsSearcher(
        searchService = DefaultFacetsSearchService(client),
        indexName = indexName,
        query = query,
        attribute = attribute,
        facetQuery = facetQuery,
        requestOptions = requestOptions,
        coroutineScope = coroutineScope,
        triggerSearchFor = triggerSearchFor
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
