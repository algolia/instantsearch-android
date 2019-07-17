package com.algolia.instantsearch.core.observable


public class ObservableItem<T>(initialValue: T) {

    internal var value: T = initialValue

    internal val listeners: MutableList<(T) -> Unit> = mutableListOf()

    public fun get() = value

    public fun set(value: T) {
        this.value = value
        listeners.forEach { it(value) }
    }

    public fun subscribe(listener: (T) -> Unit) {
        listeners += listener
    }

    public fun subscribePast(listener: (T) -> Unit) {
        listener(value)
        listeners += listener
    }

    public fun unsubscribe(listener: (T) -> Unit) {
        listeners -= listener
    }

    public fun unsubscribeAll() {
        listeners.clear()
    }
}