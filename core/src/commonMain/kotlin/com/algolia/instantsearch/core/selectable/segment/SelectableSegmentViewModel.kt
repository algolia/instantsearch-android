package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class SelectableSegmentViewModel<K, V>(
    segment: Map<K, V>
) {

    public val segment = ObservableItem(segment)
    public val selected = ObservableItem<K?>(null)
    public val eventSelection = ObservableEvent<K?>()
}