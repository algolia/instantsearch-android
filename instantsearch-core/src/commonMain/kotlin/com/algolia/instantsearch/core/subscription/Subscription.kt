package com.algolia.instantsearch.core.subscription

import com.algolia.instantsearch.core.internal.frozenCopyOnWriteSet

public open class Subscription<T> {

    internal val subscriptions: MutableSet<(T) -> Unit> = frozenCopyOnWriteSet()

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
