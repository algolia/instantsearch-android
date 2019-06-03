package com.algolia.instantsearch.core.event


public interface EventViewModel<T> {

    public val onTriggered: MutableList<(T) -> Unit>

    public fun trigger(content: T) {
        onTriggered.forEach { it(content) }
    }
}