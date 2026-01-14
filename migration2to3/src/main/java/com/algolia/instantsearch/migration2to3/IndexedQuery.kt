package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.search.SearchParamsObject
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

public sealed interface IndexedQuery {
    public val indexName: String
    public val query: SearchParamsObject
}

 /**
 * Associate a [Query] to a specific [IndexName].
 */
@InternalSerializationApi
@Serializable
public data class IndexQuery(
    override val indexName: IndexName,
    override val query: SearchParamsObject = SearchParamsObject()
) : IndexedQuery

/**
 * Associate a facets [Query] to a specific [IndexName].
 */
public class FacetIndexQuery(
    override val indexName: IndexName,
    override val query: SearchParamsObject,
    public val facetAttribute: Attribute,
    public val facetQuery: String? = null
) : IndexedQuery
