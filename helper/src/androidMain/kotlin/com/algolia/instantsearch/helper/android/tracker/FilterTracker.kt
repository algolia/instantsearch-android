@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.android.tracker

import android.content.Context
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.FilterTracker
import com.algolia.instantsearch.helper.tracker.internal.InsightsScope
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.register
import com.algolia.search.client.ClientSearch
import kotlinx.coroutines.CoroutineScope

/**
 * Creates a [FilterTracker] object.
 *
 * @param context android context
 * @param eventName default event name
 * @param searcher single index searcher
 * @param client search client for networking operations
 * @param coroutineScope coroutine scope to execute tracking operations
 */
@JvmName("create")
public fun FilterTracker(
    context: Context,
    eventName: String,
    searcher: SearcherSingleIndex,
    client: ClientSearch,
    coroutineScope: CoroutineScope = InsightsScope()
): FilterTracker {
    val insights = Insights.register(
        context = context,
        appId = client.applicationID.raw,
        apiKey = client.apiKey.raw,
        indexName = searcher.index.indexName.raw
    )

    return FilterTracker(
        eventName = eventName,
        searcher = searcher,
        insights = insights,
        coroutineScope = coroutineScope
    )
}

/**
 * Creates a [FilterTracker] object.
 *
 * @param context android context
 * @param eventName default event name
 * @param searcher multiple index searcher
 * @param pointer pointer to a specific index position
 * @param client search client for networking operations
 * @param coroutineScope coroutine scope to execute tracking operations
 */
@JvmName("create")
public fun FilterTracker(
    context: Context,
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    client: ClientSearch,
    coroutineScope: CoroutineScope = InsightsScope()
): FilterTracker {
    val insights = Insights.register(
        context = context,
        appId = client.applicationID.raw,
        apiKey = client.apiKey.raw,
        indexName = searcher.queries[pointer].indexName.raw
    )

    return FilterTracker(
        eventName = eventName,
        searcher = searcher,
        pointer = pointer,
        insights = insights,
        coroutineScope = coroutineScope
    )
}
