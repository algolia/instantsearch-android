package com.algolia.instantsearch.core.observable

import kotlin.properties.Delegates


public class SubscriptionValue<T>(initialValue: T) : Subscription<T>() {

    public var value: T  by Delegates.observable(initialValue) { _, _, newValue ->
        subscriptions.forEach { it(newValue) }
    }

    public fun subscribePast(subscription: (T) -> Unit) {
        subscription(value)
        subscriptions += subscription
    }

    public fun notifySubscriptions() {
        subscriptions.forEach { it(value) }
    }
}