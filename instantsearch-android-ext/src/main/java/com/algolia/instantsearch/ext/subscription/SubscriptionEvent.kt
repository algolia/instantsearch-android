package com.algolia.instantsearch.ext.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.ext.internal.offerCatching
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Convert SubscriptionEvent to Flow.
 */
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> SubscriptionEvent<T>.asFlow(): Flow<T> {
    return callbackFlow {
        val subscription: (T) -> Unit = { value: T ->
            offerCatching(value)
        }
        subscribe(subscription)
        awaitClose { unsubscribe(subscription) }
    }
}

/**
 * SubscriptionEvent to LiveData.
 *
 * @param context The CoroutineContext to collect the upstream flow in.
 * @param timeoutInMs The timeout in ms before cancelling the block if there are no active observers.
 */
public fun <T> SubscriptionEvent<T>.asLiveData(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L,
): LiveData<T> = asFlow().asLiveData(context, timeoutInMs)
