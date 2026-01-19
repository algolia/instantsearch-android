@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.tracker.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.client.model.search.SearchResponse

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
     * A searcher wrapper around [SearcherForHits] to enable tracking capabilities.
     */
    internal class HitsSearcher(override val searcher: SearcherForHits<*>) : TrackableSearcher<SearcherForHits<*>>() {

        override fun setClickAnalyticsOn(on: Boolean) {
            // Note: SearchParamsObject is immutable, so clickAnalytics should be set when creating the query
            // This is a no-op for now, as the query is already created
            // TODO: Consider making query mutable or setting clickAnalytics at query creation time
        }

        override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): SubscriptionJob<SearchResponse?> {
            val onChange: (SearchResponse?) -> Unit = { response ->
                subscriber.queryID = response?.queryID
            }
            return SubscriptionJob(searcher.response, onChange).also { it.start() }
        }
    }
}
