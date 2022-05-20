package com.algolia.instantsearch.coroutines

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.subscription.Subscription
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Turns this [Subscription] into a _(hot)_ [Flow] which emits whenever there is a new value.
 */
@ExperimentalInstantSearch
public fun <T> Subscription<T>.asFlow(): Flow<T> {
    return callbackFlow {
        val callback: (T) -> Unit = { event: T -> trySend(event) }
        val subscription = this@asFlow
        subscription.subscribe(callback)
        awaitClose { subscription.unsubscribe(callback) }
    }
}
