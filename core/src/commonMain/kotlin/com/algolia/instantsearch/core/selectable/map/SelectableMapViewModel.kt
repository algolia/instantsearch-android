package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.observable.SubscriptionEvent
import com.algolia.instantsearch.core.observable.SubscriptionValue


public open class SelectableMapViewModel<K, V>(
    map: Map<K, V>
): MapViewModel<K, V>(map) {

    public val selected = SubscriptionValue<K?>(null)
    public val eventSelection = SubscriptionEvent<K?>()
}