package com.algolia.instantsearch.core.observable


public class ObservableItem<T>(initialValue: T) {

    internal var value: T = initialValue

    internal val listeners: MutableMap<ObservableKey, (T) -> Unit> = mutableMapOf()

    public fun get() = value

    public fun set(value: T) {
        this.value = value
        listeners.values.forEach { it(value) }
    }

    public fun subscribe(key: ObservableKey? = null, action: (T) -> Unit) {
        listeners[key ?: ObservableKey(this)] = action
    }

    public fun subscribePast(key: ObservableKey? = null, action: (T) -> Unit) {
        action(value)
        listeners[key ?: ObservableKey(this)] = action
    }

    public fun unsubscribe(key: ObservableKey) {
        listeners.remove(key)
    }

    public fun unsubscribeAll() {
        listeners.clear()
    }
}