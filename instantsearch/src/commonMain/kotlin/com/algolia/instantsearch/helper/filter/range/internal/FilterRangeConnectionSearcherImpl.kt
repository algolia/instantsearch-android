package com.algolia.instantsearch.helper.filter.range.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.FacetStats

/**
 * Connection implementation between a Searcher and filter range components to enable a dynamic behavior.
 *
 * @param viewModel filter range view model to receive the filter stats updates.
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 * @param mapper explicit mapper to transform facets stats min/max results to the view model's bounds.
 */
internal class FilterRangeConnectionSearcherImpl<T>(
    private val viewModel: FilterRangeViewModel<T>,
    private val searcher: SearcherForHits<*>,
    private val attribute: Attribute,
    private val mapper: (Number) -> T,
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    private val responseSubscription: (ResponseSearch?) -> Unit = { response ->
        viewModel.computeBoundsFromFacetStats(attribute, response?.facetStats, mapper)
    }

    private fun <T> FilterRangeViewModel<T>.computeBoundsFromFacetStats(
        attribute: Attribute,
        facetStats: Map<Attribute, FacetStats>?,
        mapper: (Number) -> T,
    ) where T : Number, T : Comparable<T> {
        bounds.value = facetStats?.get(attribute)?.let {
            val min = mapper(it.min)
            val max = mapper(it.max)
            Range(min, max)
        }
    }

    override fun connect() {
        super.connect()
        searcher.query.updateQueryFacets(attribute)
        searcher.response.subscribePast(responseSubscription)
    }

    private fun CommonSearchParameters.updateQueryFacets(attribute: Attribute) {
        val current = facets?.toMutableSet() ?: mutableSetOf()
        facets = if (!current.contains(attribute)) current + attribute else setOf(attribute)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(responseSubscription)
    }
}
