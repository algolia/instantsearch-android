package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import com.algolia.instantsearch.core.observable.SubscriptionValue


public open class SelectableItemViewModel<T>(
    item: T
) {

    public val item = SubscriptionValue(item)
    public val isSelected = SubscriptionValue(false)
    public val eventSelection = SubscriptionEvent<Boolean>()
}