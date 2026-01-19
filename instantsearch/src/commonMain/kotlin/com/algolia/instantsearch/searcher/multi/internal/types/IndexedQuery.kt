package com.algolia.instantsearch.searcher.multi.internal.types

import com.algolia.client.model.search.SearchParamsObject
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

public sealed interface IndexedQuery {
    public val indexName: String
    public val query: SearchParamsObject
}

/**
 * Associate a [SearchParamsObject] to a specific indexName.
 */
@InternalSerializationApi
@Serializable
public data class IndexQuery(
    override val indexName: String,
    override val query: SearchParamsObject = SearchParamsObject()
) : IndexedQuery

/**
 * Associate a facets [SearchParamsObject] to a specific indexName.
 */
public class FacetIndexQuery(
    override val indexName: String,
    override val query: SearchParamsObject,
    public val facetAttribute: String,
    public val facetQuery: String? = null
) : IndexedQuery

