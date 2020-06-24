package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.core.subscription.Subscription
import com.algolia.instantsearch.core.subscription.SubscriptionValue

/**
 * Represents a link between a [Subscription] and a subscriber.
 */
public class SubscriptionJob<T>(
    private val subscription: Subscription<T>,
    private val subscriber: (T) -> Unit
) {

    /**
     * Current state, true if active, otherwise false.
     */
    public var isActive: Boolean = false
        internal set

    /**
     * Subscribe the subscriber to the subscription.
     */
    public fun start() {
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
    public fun cancel() {
        if (!isActive) return
        subscription.unsubscribe(subscriber)
        isActive = false
    }
}
