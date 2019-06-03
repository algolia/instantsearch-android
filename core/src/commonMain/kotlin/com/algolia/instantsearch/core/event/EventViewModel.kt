package com.algolia.instantsearch.core.event


public class EventViewModel<T> {

    public val onTriggered: MutableList<(T) -> Unit> = mutableListOf()

    public fun trigger(content: T) {
        onTriggered.forEach { it(content) }
    }
}