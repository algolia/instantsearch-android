package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue


public open class SelectableItemViewModel<T>(
    item: T,
    isSelected: Boolean = false
) {

    public val item: SubscriptionValue<T> = SubscriptionValue(item)
    public val isSelected: SubscriptionValue<Boolean> = SubscriptionValue(isSelected)
    public val eventSelection: SubscriptionEvent<Boolean> = SubscriptionEvent<Boolean>()
}
