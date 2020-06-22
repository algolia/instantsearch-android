package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * A searcher wrapper to enable tracking capabilities.
 */
public sealed class TrackableSearcher {

    /**
     * Enable the Click Analytics feature.
     */
    public abstract fun setClickAnalyticsOn(on: Boolean)

    /**
     * Subscribe for the Query ID changes.
     */
    public abstract fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T)

    /**
     * A searcher wrapper around [SearcherSingleIndex] to enable tracking capabilities.
     */
    public class SingleIndex(private val searcher: SearcherSingleIndex) : TrackableSearcher() {

        public override fun setClickAnalyticsOn(on: Boolean) {
            searcher.query.clickAnalytics = on
        }

        public override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T) {
            searcher.response.subscribePast { response ->
                subscriber.queryID = response?.queryID?.raw
            }
        }
    }

    /**
     * A searcher wrapper around [SearcherMultipleIndex] to enable tracking capabilities.
     */
    public class MultiIndex(
        private val searcher: SearcherMultipleIndex, private val pointer: Int
    ) : TrackableSearcher() {

        public override fun setClickAnalyticsOn(on: Boolean) {
            searcher.queries[pointer].query.clickAnalytics = on
        }

        public override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T) {
            searcher.response.subscribePast { response ->
                subscriber.queryID = response?.results?.get(pointer)?.queryID?.raw
            }
        }
    }
}
