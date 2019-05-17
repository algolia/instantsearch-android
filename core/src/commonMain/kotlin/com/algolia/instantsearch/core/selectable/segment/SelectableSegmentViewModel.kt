package com.algolia.instantsearch.core.selectable.segment

import kotlin.properties.Delegates


public open class SelectableSegmentViewModel<K, V>(
    val items: Map<K, V>
) {

    public val onSelectedChanged: MutableList<(K?) -> Unit> = mutableListOf()
    public val onSelectedComputed: MutableList<(K?) -> Unit> = mutableListOf()

    public var selected by Delegates.observable<K?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun computeSelected(selected: K) {
        onSelectedComputed.forEach { it(selected) }
    }
}
