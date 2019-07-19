package com.algolia.instantsearch.core.observable

import kotlin.properties.Delegates


public class ObservableItem<T>(initialValue: T) {

    internal val subscriptions: MutableList<(T) -> Unit> = mutableListOf()

    public var value: T  by Delegates.observable(initialValue) { _, _, newValue ->
        subscriptions.forEach { it(newValue) }
    }

    public fun subscribe(listener: (T) -> Unit) {
        subscriptions += listener
    }

    public fun subscribePast(listener: (T) -> Unit) {
        listener(value)
        subscriptions += listener
    }

    public fun unsubscribe(listener: (T) -> Unit) {
        subscriptions -= listener
    }

    public fun unsubscribeAll() {
        subscriptions.clear()
    }

    public fun notifySubscriptions() {
        subscriptions.forEach { it(value) }
    }
}