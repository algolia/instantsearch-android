package com.algolia.instantsearch.core.subscription

import java.util.concurrent.CopyOnWriteArraySet

public open class Subscription<T> {

    internal val subscriptions: MutableSet<(T) -> Unit> = CopyOnWriteArraySet()

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

    public fun notifyAll(value: T) {
        subscriptions.onEach { it(value) }
    }
}
