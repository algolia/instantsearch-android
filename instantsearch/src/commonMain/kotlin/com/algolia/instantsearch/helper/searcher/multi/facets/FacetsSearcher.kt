package com.algolia.instantsearch.helper.searcher.multi.facets

import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.multi.MultiSearchComponent
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearcherImpl
import com.algolia.instantsearch.helper.searcher.multi.facets.internal.FacetsSearcherImpl
import com.algolia.instantsearch.helper.searcher.multi.internal.asMultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.FacetIndexQuery
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
public interface FacetsSearcher : MultiSearchComponent<FacetIndexQuery, ResponseSearchForFacets>

/**
 * Creates an instance of [FacetsSearcher].
 */
public fun FacetsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null,
    coroutineScope: CoroutineScope = SearcherScope(),
): FacetsSearcher = FacetsSearcherImpl(
    client = client,
    indexedQuery = FacetIndexQuery(indexName, query, attribute, facetQuery),
    requestOptions = requestOptions,
    coroutineScope = coroutineScope,
)

/**
 * Adds a [FacetsSearcher].
 */
fun MultiSearcherImpl.addFacetsSearcher(
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String? = null,
    requestOptions: RequestOptions? = null
): FacetsSearcher {
    return FacetsSearcher(
        client = client,
        indexName = indexName,
        attribute = attribute,
        query = query,
        facetQuery = facetQuery,
        requestOptions = requestOptions,
        coroutineScope = coroutineScope
    ).also { addSearcher(it.asMultiSearchComponent()) }
}
