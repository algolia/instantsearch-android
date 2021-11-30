package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.internal.traceSortBy
import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue

public open class SelectableMapViewModel<K, V>(
    map: Map<K, V> = emptyMap(),
    selected: K? = null
) : MapViewModel<K, V>(map) {

    init {
        traceSortBy()
    }

    public val selected: SubscriptionValue<K?> = SubscriptionValue(selected)
    public val eventSelection: SubscriptionEvent<K?> = SubscriptionEvent<K?>()
}
