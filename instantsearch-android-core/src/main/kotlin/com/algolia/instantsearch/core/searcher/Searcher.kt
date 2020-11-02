package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * The Searcher is the main component of InstantSearch Android.
 * It wraps an Algolia API Client and provides a level of abstraction over it.
 */
public interface Searcher<R> {

    public val coroutineScope: CoroutineScope

    public val isLoading: SubscriptionValue<Boolean>
    public val error: SubscriptionValue<Throwable?>
    public val response: SubscriptionValue<R?>

    public fun setQuery(text: String?)
    public fun searchAsync(): Job
    public suspend fun search(): R
    public fun cancel()
}
