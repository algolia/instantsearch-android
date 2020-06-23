package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearches

/**
 * A searcher wrapper to enable tracking capabilities.
 */
public sealed class TrackableSearcher<T> where T : Searcher<*> {

    internal abstract val searcher: T

    /**
     * Enable the Click Analytics feature.
     */
    public abstract fun setClickAnalyticsOn(on: Boolean)

    /**
     * Subscribe for the Query ID changes.
     */
    public abstract fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): TrackingSubscription<*>

    /**
     * A searcher wrapper around [SearcherSingleIndex] to enable tracking capabilities.
     */
    public class SingleIndex(override val searcher: SearcherSingleIndex) : TrackableSearcher<SearcherSingleIndex>() {

        public override fun setClickAnalyticsOn(on: Boolean) {
            searcher.query.clickAnalytics = on
        }

        public override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): TrackingSubscription<ResponseSearch?> {
            val subscription: (ResponseSearch?) -> Unit = { response ->
                subscriber.queryID = response?.queryID
            }
            searcher.response.subscribePast(subscription)
            return TrackingSubscription(searcher.response, subscription)
        }
    }

    /**
     * A searcher wrapper around [SearcherMultipleIndex] to enable tracking capabilities.
     */
    public class MultiIndex(
        override val searcher: SearcherMultipleIndex,
        private val pointer: Int
    ) : TrackableSearcher<SearcherMultipleIndex>() {

        public override fun setClickAnalyticsOn(on: Boolean) {
            searcher.queries[pointer].query.clickAnalytics = on
        }

        public override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T): TrackingSubscription<ResponseSearches?> {
            val subscription: (ResponseSearches?) -> Unit = { response ->
                subscriber.queryID = response?.results?.get(pointer)?.queryID
            }
            searcher.response.subscribePast(subscription)
            return TrackingSubscription(searcher.response, subscription)
        }
    }
}
