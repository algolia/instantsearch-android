package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.jvm.JvmSynthetic

/**
 * A component that can [search] Algolia for a given [query][setQuery].
 *
 * @param R The type of the search [response].
 */
public interface Searcher<R> {

    /**
     * The scope used for launching network coroutines.
     */
    public val coroutineScope: CoroutineScope
        @JvmSynthetic get

    /**
     * A SubscriptionValue that is `true` when there are pending requests.
     */
    public val isLoading: SubscriptionValue<Boolean>
    /**
     * A SubscriptionValue that holds any error encountered during a search.
     */
    public val error: SubscriptionValue<Throwable?>
    /**
     * A SubscriptionValue that holds the last search response received.
     */
    public val response: SubscriptionValue<R?>

    /**
     * Sets the query for future searches.
     *
     * @param text the content to search for.
     */
    public fun setQuery(text: String?)

    /**
     * Triggers an asynchronous search request.
     *
     * @return a [Job] for the started operation.
     */
    public fun searchAsync(): Job

    /**
     * Triggers a synchronous search request.
     *
     * @return the search response.
     */
    public suspend fun search(): R

    /**
     * Cancels any ongoing request.
     */
    public fun cancel()
}