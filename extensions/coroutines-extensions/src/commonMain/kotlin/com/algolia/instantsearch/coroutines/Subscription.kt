package com.algolia.instantsearch.coroutines

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.subscription.Subscription
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 * Turns this [Subscription] into a _(hot)_ [Flow] which emits whenever there is a new value.
 */
@ExperimentalInstantSearch
public fun <T> Subscription<T>.asFlow(): Flow<T> {
    @OptIn(ExperimentalCoroutinesApi::class)
    return callbackFlow {
        val callback: (T) -> Unit = { event: T -> trySend(event) }
        val subscription = this@asFlow
        subscription.subscribe(callback)
        awaitClose { subscription.unsubscribe(callback) }
    }
}

/**
 * Turns this [SubscriptionValue] into a [StateFlow] which emits whenever there is a new value.
 *
 * @param scope the coroutine scope in which sharing is started
 */
@ExperimentalInstantSearch
public fun <T> SubscriptionValue<T>.stateIn(scope: CoroutineScope): StateFlow<T> {
    return MutableStateFlow(value).also { stateFlow ->
        val callback: (T) -> Unit = { stateFlow.value = it }
        scope.launch {
            subscribe(callback)
            awaitCancellation()
        }.invokeOnCompletion {
            unsubscribe(callback)
        }
    }
}
