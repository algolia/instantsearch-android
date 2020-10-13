package com.algolia.instantsearch.core.subscription

public open class Subscription<T> {

    internal val subscriptions: MutableSet<(T) -> Unit> = mutableSetOf()

    public fun subscribe(subscription: (T) -> Unit) {
        subscriptions += subscription
    }

    public fun subscribe(subscription: Collection<(T) -> Unit>) {
        subscriptions += subscription
    }

    public fun unsubscribe(subscription: (T) -> Unit) {
        subscriptions -= subscription
    }

    public fun unsubscribe(subscription: Collection<(T) -> Unit>) {
        subscriptions -= subscription
    }

    public fun unsubscribeAll() {
        subscriptions.clear()
    }
}
