@file:JvmName("SelectableItem")

package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing an item that can be selected.
 * @param item the initial value.
 * @param isSelected if `true`, the ViewModel starts in a selected state.
 */
public open class SelectableItemViewModel<T> @JvmOverloads constructor(
    item: T,
    isSelected: Boolean = false
) {

    /**
     * The current item.
     */
    @JvmField
    public val item = SubscriptionValue(item)
    /**
     * The selection state of the item.
     */
    @JvmField
    public val isSelected = SubscriptionValue(isSelected)
    /**
     * Event fired whenever the selection changes.
     */
    @JvmField
    public val eventSelection = SubscriptionEvent<Boolean>()
}