@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.android.tracker

import android.content.Context
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.register
import com.algolia.search.client.ClientSearch

@JvmName("create")
public fun HitsTracker(
    context: Context,
    eventName: String,
    searcher: SearcherSingleIndex,
    client: ClientSearch
): HitsTracker {
    val insights = Insights.register(
        context = context,
        appId = client.applicationID.raw,
        apiKey = client.apiKey.raw,
        indexName = searcher.index.indexName.raw
    )

    return HitsTracker(
        eventName = eventName,
        searcher = searcher,
        insights = insights
    )
}

@JvmName("create")
public fun HitsTracker(
    context: Context,
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    client: ClientSearch
): HitsTracker {
    val insights = Insights.register(
        context = context,
        appId = client.applicationID.raw,
        apiKey = client.apiKey.raw,
        indexName = searcher.queries[pointer].indexName.raw
    )

    return HitsTracker(
        eventName = eventName,
        searcher = searcher,
        pointer = pointer,
        insights = insights
    )
}
