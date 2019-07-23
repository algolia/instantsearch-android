package com.algolia.instantsearch.core.observable


public class SubscriptionEvent<T> : Subscription<T>() {

    public fun send(event: T) {
        subscriptions.forEach { it(event) }
    }
}