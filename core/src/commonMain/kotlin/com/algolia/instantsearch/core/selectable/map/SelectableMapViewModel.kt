package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing a map of items, where any single item can be selected.
 *
 * @param map the initial values.
 * @param selected the key of an eventual item selected initially.
 * @param K the type of the keys used to identify items.
 * @param V the type of the items.
 */
public open class SelectableMapViewModel<K, V> @JvmOverloads constructor(
    map: Map<K, V> = mapOf(),
    selected: K? = null
) : MapViewModel<K, V>(map) {

    @JvmField
    public val selected = SubscriptionValue(selected)
    @JvmField
    public val eventSelection = SubscriptionEvent<K?>()
}