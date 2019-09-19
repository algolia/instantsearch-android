package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import kotlin.jvm.JvmField

/**
 * A ViewModel notifying when filters get cleared.
 */
public open class FilterClearViewModel {

    @JvmField
    public val eventClear = SubscriptionEvent<Unit>()
}