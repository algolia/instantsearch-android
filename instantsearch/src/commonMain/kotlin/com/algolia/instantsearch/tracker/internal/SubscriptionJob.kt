package com.algolia.instantsearch.tracker.internal

import com.algolia.instantsearch.core.subscription.Subscription
import com.algolia.instantsearch.core.subscription.SubscriptionValue

/**
 * Represents a link between a [Subscription] and a subscriber.
 */
internal class SubscriptionJob<T>(
    private val subscription: Subscription<T>,
    private val subscriber: (T) -> Unit
) {

    /**
     * Current state, true if active, otherwise false.
     */
    internal var isActive: Boolean = false
        internal set

    /**
     * Subscribe the subscriber to the subscription.
     */
    internal fun start() {
        if (isActive) return
        when (subscription) {
            is SubscriptionValue -> subscription.subscribePast(subscriber)
            else -> subscription.subscribe(subscriber)
        }
        isActive = true
    }

    /**
     * Unsubscribe the subscriber from the subscription.
     */
    internal fun cancel() {
        if (!isActive) return
        subscription.unsubscribe(subscriber)
        isActive = false
    }
}
