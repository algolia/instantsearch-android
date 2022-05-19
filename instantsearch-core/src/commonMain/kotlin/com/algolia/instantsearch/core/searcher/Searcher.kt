package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * The Searcher is the main component of InstantSearch Android.
 * It wraps an Algolia API Client and provides a level of abstraction over it.
 */
public interface Searcher<R> {

    /**
     * Searcher's coroutine scope for asynchronous search.
     */
    public val coroutineScope: CoroutineScope

    /**
     * Search operations dispatcher.
     */
    public val coroutineDispatcher: CoroutineDispatcher

    /**
     * Loading status subscription value.
     */
    public val isLoading: SubscriptionValue<Boolean>

    /**
     * Error subscription value. Notifies all subscribers when an error occurs.
     */
    public val error: SubscriptionValue<Throwable?>

    /**
     * Error subscription value. Notifies all subscribers when a response is received.
     */
    public val response: SubscriptionValue<R?>

    /**
     * Sets the query to the string provided.
     */
    public fun setQuery(text: String?)

    /**
     * Triggers asynchronous search operation with [coroutineScope].
     * Updates [response]/[error] on result.
     */
    public fun searchAsync(): Job

    /**
     * Triggers the search and returns a search response.
     * The search is executed on the [coroutineDispatcher].
     */
    public suspend fun search(): R?

    /**
     * Cancels the ongoing search requests.
     */
    public fun cancel()
}
