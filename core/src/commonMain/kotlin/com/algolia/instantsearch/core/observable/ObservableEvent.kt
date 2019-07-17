package com.algolia.instantsearch.core.observable


public class ObservableEvent<T> {

    internal val listeners: MutableList<(T) -> Unit> = mutableListOf()

    public fun send(event: T) {
        listeners.forEach { it(event) }
    }

    public fun subscribe(listener: (T) -> Unit) {
        listeners += listener
    }

    public fun unsubscribe(listener: (T) -> Unit) {
        listeners -= listener
    }

    public fun unsubscribeAll() {
        listeners.clear()
    }
}