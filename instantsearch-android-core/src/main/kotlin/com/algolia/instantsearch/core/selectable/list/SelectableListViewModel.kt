package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.selectable.list.SelectionMode.Multiple
import com.algolia.instantsearch.core.selectable.list.SelectionMode.Single
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue

public open class SelectableListViewModel<K, V>(
    items: List<V> = listOf(),
    public val selectionMode: SelectionMode
) {

    public val items: SubscriptionValue<List<V>> = SubscriptionValue(items)
    public val selections: SubscriptionValue<Set<K>> = SubscriptionValue(setOf())
    public val eventSelection: SubscriptionEvent<Set<K>> = SubscriptionEvent()

    public fun select(key: K) {
        val selections = selections.value
        val event = when (selectionMode) {
            Single -> if (key in selections) setOf() else setOf(key)
            Multiple -> if (key in selections) selections - key else selections + key
        }

        eventSelection.send(event)
    }
}
