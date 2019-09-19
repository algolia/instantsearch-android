package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.selectable.list.SelectionMode.Multiple
import com.algolia.instantsearch.core.selectable.list.SelectionMode.Single
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing a list of items, that can be selected according to a [mode][SelectionMode].
 *
 * @param items the initial values.
 * @param selectionMode the mode of selection to use.
 * @param K the type of the keys used to identify items.
 * @param V the type of the items.
 */
public open class SelectableListViewModel<K, V> @JvmOverloads constructor(
    items: List<V> = listOf(),
    @JvmField
    public val selectionMode: SelectionMode
) {

    @JvmField
    public val items = SubscriptionValue(items)
    @JvmField
    public val selections = SubscriptionValue<Set<K>>(setOf())
    @JvmField
    public val eventSelection = SubscriptionEvent<Set<K>>()

    /**
     * Selects one of the [items] based on its [key].
     */
    public fun select(key: K) {
        val selections = selections.value
        val event = when (selectionMode) {
            Single -> if (key in selections) setOf() else setOf(key)
            Multiple -> if (key in selections) selections - key else selections + key
        }

        eventSelection.send(event)
    }
}