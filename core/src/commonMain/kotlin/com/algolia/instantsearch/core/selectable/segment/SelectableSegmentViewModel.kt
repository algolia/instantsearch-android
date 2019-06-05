package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.map.MapViewModel
import kotlin.properties.Delegates


public open class SelectableSegmentViewModel<K, V>(
    items: Map<K, V>
) : MapViewModel<K, V>(items) {

    public val onSelectedChanged: MutableList<(K?) -> Unit> = mutableListOf()
    public val onSelectedComputed: MutableList<(K?) -> Unit> = mutableListOf()

    public var selected by Delegates.observable<K?>(null) { _, _, newValue ->
        onSelectedChanged.forEach { it(newValue) }
    }

    public fun computeSelected(selected: K) {
        onSelectedComputed.forEach { it(selected) }
    }
}
