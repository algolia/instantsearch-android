package com.algolia.instantsearch.helper.searcher.facets

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.facets.internal.DefaultFacetsSearcher
import com.algolia.instantsearch.helper.searcher.util.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
@ExperimentalInstantSearch
public interface FacetsSearcher : SearcherForFacets<Query>, IndexNameHolder {

    /**
     * Facets attribute.
     */
    public val attribute: Attribute

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
@ExperimentalInstantSearch
public fun FacetsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
): FacetsSearcher = DefaultFacetsSearcher(
    client = client,
    indexName = indexName,
    query = query,
    attribute = attribute,
    facetQuery = facetQuery,
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
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
@ExperimentalInstantSearch
public fun MultiSearcher.addFacetsSearcher(
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null
): FacetsSearcher {
    return DefaultFacetsSearcher(
        client = client,
        indexName = indexName,
        query = query,
        attribute = attribute,
        facetQuery = facetQuery,
        requestOptions = requestOptions,
        coroutineScope = coroutineScope,
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
