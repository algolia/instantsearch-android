@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.tracker.internal

import com.algolia.client.model.search.SearchParamsObject
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
            val typedSearcher = searcher as? SearcherForHits<SearchParamsObject> ?: return
            typedSearcher.query = typedSearcher.query.copy(clickAnalytics = on)
        }

        override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): SubscriptionJob<SearchResponse?> {
            val onChange: (SearchResponse?) -> Unit = { response ->
                subscriber.queryID = response?.queryID
            }
            return SubscriptionJob(searcher.response, onChange).also { it.start() }
        }
    }
}
