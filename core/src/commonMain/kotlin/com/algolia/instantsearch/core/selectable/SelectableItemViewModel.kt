package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue


public open class SelectableItemViewModel<T>(
    item: T
) {

    public val item = SubscriptionValue(item)
    public val isSelected = SubscriptionValue(false)
    public val eventSelection = SubscriptionEvent<Boolean>()
}