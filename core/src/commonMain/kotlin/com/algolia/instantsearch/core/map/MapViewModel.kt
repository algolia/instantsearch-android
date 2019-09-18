package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField


/**
 * A ViewModel storing a map of items, where any single item can be selected.
 *
 * @param items the initial values.
 * @param K the type of the keys used to identify items.
 * @param V the type of the items.
 */
public open class MapViewModel<K, V>(
    items: Map<K, V> = mapOf()
) {

    @JvmField
    public val map = SubscriptionValue(items)
    @JvmField
    public val event = SubscriptionEvent<Map<K, V>>()

    public fun add(entry: Pair<K, V>) {
        val map = map.value.toMutableMap().apply { put(entry.first, entry.second) }

        event.send(map)
    }

    public fun remove(key: K) {
        val map = map.value.toMutableMap().apply { remove(key) }

        event.send(map)
    }

    public fun clear() {
        val map = map.value.toMutableMap().apply { clear() }

        event.send(map)
    }
}