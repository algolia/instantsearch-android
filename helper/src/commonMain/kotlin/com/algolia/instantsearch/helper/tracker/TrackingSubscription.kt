package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.core.subscription.Subscription

public class TrackingSubscription<T>(
    private val subscription: Subscription<T>,
    private val subscriber: (T) -> Unit
) {

    public fun cancel() {
        subscription.unsubscribe(subscriber)
    }
}
