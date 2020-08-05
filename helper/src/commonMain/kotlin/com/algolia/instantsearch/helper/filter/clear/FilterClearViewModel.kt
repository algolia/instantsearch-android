package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.subscription.SubscriptionEvent


public open class FilterClearViewModel {

    public val eventClear: SubscriptionEvent<Unit> = SubscriptionEvent()
}
