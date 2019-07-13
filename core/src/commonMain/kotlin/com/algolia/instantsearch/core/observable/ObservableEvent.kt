package com.algolia.instantsearch.core.observable


public class ObservableEvent<T> {

    internal val listeners: MutableMap<ObservableKey, (T) -> Unit> = mutableMapOf()

    public fun send(event: T) {
        listeners.values.forEach { it(event) }
    }

    public fun subscribe(key: ObservableKey? = null, action: (T) -> Unit) {
        listeners[key ?: ObservableKey(this)] = action
    }

    public fun unsubscribe(key: ObservableKey?) {
        listeners.remove(key)
    }

    public fun unsubscribeAll() {
        listeners.clear()
    }
}