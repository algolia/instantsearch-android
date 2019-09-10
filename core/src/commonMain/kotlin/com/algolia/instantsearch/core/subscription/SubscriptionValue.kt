package com.algolia.instantsearch.core.subscription

import kotlin.properties.Delegates

/**
 * A Value that you can subscribe to with a callback, which gets called when the value changes.
 */
public class SubscriptionValue<T>(initialValue: T) : Subscription<T>() {

    public var value: T by Delegates.observable(initialValue) { _, _, newValue ->
        subscriptions.forEach { it(newValue) }
    }

    public fun subscribePast(subscription: (T) -> Unit) {
        subscription(value)
        subscribe(subscription)
    }

    public fun notifySubscriptions() {
        subscriptions.forEach { it(value) }
    }
}