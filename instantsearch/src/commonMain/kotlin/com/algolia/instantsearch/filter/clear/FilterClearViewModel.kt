package com.algolia.instantsearch.filter.clear

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.extension.traceFilterClear

public open class FilterClearViewModel {

    init {
        traceFilterClear()
    }

    public val eventClear: SubscriptionEvent<Unit> = SubscriptionEvent()
}
