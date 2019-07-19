package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.instantsearch.core.selectable.list.SelectionMode.Multiple
import com.algolia.instantsearch.core.selectable.list.SelectionMode.Single


public open class SelectableListViewModel<K, V>(
    items: List<V> = listOf(),
    public val selectionMode: SelectionMode
) {

    public val items = ObservableItem(items)
    public val selections = ObservableItem<Set<K>>(setOf())
    public val eventSelection = ObservableEvent<Set<K>>()

    public fun select(key: K) {
        val selections = selections.value
        val event = when (selectionMode) {
            Single -> if (key in selections) setOf() else setOf(key)
            Multiple -> if (key in selections) selections - key else selections + key
        }

        eventSelection.send(event)
    }
}