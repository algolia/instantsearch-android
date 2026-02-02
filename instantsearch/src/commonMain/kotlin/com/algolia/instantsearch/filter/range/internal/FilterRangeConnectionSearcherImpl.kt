package com.algolia.instantsearch.filter.range.internal

import com.algolia.client.model.search.FacetStats
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.searcher.updateSearchParamsObject
import com.algolia.instantsearch.searcher.updateQueryFacets

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
    private val attribute: String,
    private val mapper: (Number) -> T,
) : AbstractConnection() where T : Number, T : Comparable<T> {

    private val responseSubscription: (SearchResponse?) -> Unit = { response ->
        viewModel.computeBoundsFromFacetStats(
            attribute = attribute,
            facetStats = response?.facetsStats,
            mapper = mapper
        )
    }

    private fun <T> FilterRangeViewModel<T>.computeBoundsFromFacetStats(
        attribute: String,
        facetStats: Map<String, FacetStats>?,
        mapper: (Number) -> T,
    ) where T : Number, T : Comparable<T> {
        bounds.value = facetStats?.get(attribute)?.let {
            val min = mapper(it.min ?: 0)
            val max = mapper(it.max ?: 0)
            Range(min, max)
        }
        if (range.value == null) { // if no range is specified, match the bounds.
            range.value = bounds.value
        }
    }

    override fun connect() {
        super.connect()
        searcher.updateSearchParamsObject { it.updateQueryFacets(attribute) }
        searcher.response.subscribePastOnce(subscription = responseSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(responseSubscription)
    }
}
