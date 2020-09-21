package com.algolia.instantsearch.core.subscription

public class SubscriptionEvent<T> : Subscription<T>() {

    public fun send(event: T) {
        subscriptions.forEach { it(event) }
    }
}
