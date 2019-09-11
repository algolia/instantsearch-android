package com.algolia.instantsearch.core.subscription

import com.algolia.instantsearch.core.Callback
import kotlin.properties.Delegates

/**
 * A Value that you can subscribe to with a callback, which gets called when the value changes.
 */
public class SubscriptionValue<T>(initialValue: T) : Subscription<T>() {

    public var value: T by Delegates.observable(initialValue) { _, _, newValue ->
        subscriptions.forEach { it(newValue) }
    }

    public fun subscribePast(subscription: com.algolia.instantsearch.core.Callback<T>) {
        subscription(value)
        subscribe(subscription)
    }

    public fun notifySubscriptions() {
        subscriptions.forEach { it(value) }
    }
}