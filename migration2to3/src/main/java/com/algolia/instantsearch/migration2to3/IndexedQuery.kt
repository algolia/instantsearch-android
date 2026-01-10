package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

public sealed interface IndexedQuery {
    public val indexName: IndexName
    public val query: Query
}

 /**
 * Associate a [Query] to a specific [IndexName].
 */
@InternalSerializationApi
@Serializable
public data class IndexQuery(
    override val indexName: IndexName,
    override val query: Query = Query()
) : IndexedQuery

/**
 * Associate a facets [Query] to a specific [IndexName].
 */
public class FacetIndexQuery(
    override val indexName: IndexName,
    override val query: Query,
    public val facetAttribute: Attribute,
    public val facetQuery: String? = null
) : IndexedQuery
