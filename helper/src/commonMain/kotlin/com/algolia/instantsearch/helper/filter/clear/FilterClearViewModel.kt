package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.observable.SubscriptionEvent


public open class FilterClearViewModel {

    public val eventClear = SubscriptionEvent<Unit>()
}