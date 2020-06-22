package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * A searcher with tracking capabilities.
 */
public sealed class TrackableSearcher {

    public abstract val indexName: String

    public abstract fun setClickAnalyticsOn(on: Boolean)

    public abstract fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T)

    public class SingleIndex(private val searcher: SearcherSingleIndex) : TrackableSearcher() {

        public override val indexName: String
            get() = searcher.index.indexName.raw // indexQueryState?

        public override fun setClickAnalyticsOn(on: Boolean) {
            searcher.query.clickAnalytics = on
        }

        public override fun <T : QueryIDContainer> subscribeForQueryIDChange(subscriber: T) {
            searcher.response.subscribePast { response ->
                subscriber.queryID = response?.queryID?.raw
            }
        }
    }

    public class MultiIndex(private val searcher: SearcherMultipleIndex, private val pointer: Int) :
        TrackableSearcher() {
        public override val indexName: String
            get() = searcher.queries[pointer].indexName.raw

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
