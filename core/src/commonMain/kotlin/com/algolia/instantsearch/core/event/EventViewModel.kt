package com.algolia.instantsearch.core.event


public interface EventViewModel<T> {

    public val onTriggered: MutableList<(T) -> Unit>

    public fun trigger(event: T) {
        onTriggered.forEach { it(event) }
    }
}