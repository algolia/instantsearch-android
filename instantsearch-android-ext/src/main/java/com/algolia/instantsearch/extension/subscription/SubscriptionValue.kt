package com.algolia.instantsearch.extension.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.internal.offerCatching
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> SubscriptionValue<T>.asFlow(past: Boolean = false): Flow<T> {
    return callbackFlow {
        val subscription: (T) -> Unit = { value: T ->
            offerCatching(value)
        }
        if (past) subscribePast(subscription) else subscribe(subscription)
        awaitClose { unsubscribe(subscription) }
    }
}

/**
 * @param context The CoroutineContext to collect the upstream flow in.
 * @param timeoutInMs The timeout in ms before cancelling the block if there are no active observers.
 */
public fun <T> SubscriptionValue<T>.asLiveData(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L,
    past: Boolean = false,
): LiveData<T> = asFlow(past = past).asLiveData(context, timeoutInMs)
