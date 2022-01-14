@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.util.SearcherForHits
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearches

/**
 * A searcher wrapper to enable tracking capabilities.
 */
internal sealed class TrackableSearcher<T> where T : Searcher<*> {

    /**
     * Wrapped searcher.
     */
    internal abstract val searcher: T

    /**
     * Enable the Click Analytics feature.
     *
     * @param on true to enable click analytics feature, false to disable it.
     */
    internal abstract fun setClickAnalyticsOn(on: Boolean)

    /**
     * Subscribe for the Query ID changes.
     *
     * @param subscriber subscriber for query ID change tracking.
     */
    internal abstract fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): SubscriptionJob<*>

    /**
     * A searcher wrapper around [SearcherSingleIndex] to enable tracking capabilities.
     */
    internal class HitsSearcher(override val searcher: SearcherForHits<*>) : TrackableSearcher<SearcherForHits<*>>() {

        override fun setClickAnalyticsOn(on: Boolean) {
            searcher.query.clickAnalytics = on
        }

        override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): SubscriptionJob<ResponseSearch?> {
            val onChange: (ResponseSearch?) -> Unit = { response ->
                subscriber.queryID = response?.queryID
            }
            return SubscriptionJob(searcher.response, onChange).also { it.start() }
        }
    }

    /**
     * A searcher wrapper around [SearcherMultipleIndex] to enable tracking capabilities.
     */
    @Deprecated("Use multiple HitsSearcher aggregated with MultiSearcher instead of SearcherMultipleIndex")
    internal class MultiIndex(
        override val searcher: SearcherMultipleIndex,
        private val pointer: Int
    ) : TrackableSearcher<SearcherMultipleIndex>() {

        internal override fun setClickAnalyticsOn(on: Boolean) {
            searcher.queries[pointer].query.clickAnalytics = on
        }

        internal override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): SubscriptionJob<ResponseSearches?> {
            val onChange: (ResponseSearches?) -> Unit = { response ->
                subscriber.queryID = response?.results?.get(pointer)?.queryID
            }
            return SubscriptionJob(searcher.response, onChange).also { it.start() }
        }
    }
}
