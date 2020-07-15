@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.range.internal.mapper
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.FacetStats
import com.algolia.search.model.search.Query
import kotlin.reflect.KClass

internal class FilterRangeConnectionSearcherImpl<T>(
    private val filterRangeViewModel: FilterRangeViewModel<T>,
    private val searcher: SearcherSingleIndex,
    private val attribute: Attribute,
    private val mapper: (Number) -> T
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    val subscription: (ResponseSearch?) -> Unit = { response ->
        filterRangeViewModel.computeBoundsFromFacetStats(attribute, response?.facetStats, mapper)
    }

    private fun <T> FilterRangeViewModel<T>.computeBoundsFromFacetStats(
        attribute: Attribute,
        facetStats: Map<Attribute, FacetStats>?,
        mapper: (Number) -> T
    ) where T : Number, T : Comparable<T> {

        val facetStatsOfAttribute = facetStats?.get(attribute)
        if (facetStatsOfAttribute == null) {
            bounds.value = null
            return
        }

        val min = mapper(facetStatsOfAttribute.min)
        val max = mapper(facetStatsOfAttribute.max)

        bounds.value = Range(min, max)
    }

    override fun connect() {
        super.connect()
        searcher.query.updateQueryFacets(attribute)
        searcher.response.subscribePast(subscription)
    }

    private fun Query.updateQueryFacets(attribute: Attribute) {
        facets = if (facets?.contains(attribute) == false) {
            val current = facets?.toMutableSet() ?: mutableSetOf()
            current + attribute
        } else {
            setOf(attribute)
        }
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(subscription)
    }
}

public fun <T> FilterRangeConnectionSearcher(
    filterRangeViewModel: FilterRangeViewModel<T>,
    searcher: SearcherSingleIndex,
    attribute: Attribute,
    clazz: KClass<T>
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcherImpl(filterRangeViewModel, searcher, attribute, mapper(clazz))
}
