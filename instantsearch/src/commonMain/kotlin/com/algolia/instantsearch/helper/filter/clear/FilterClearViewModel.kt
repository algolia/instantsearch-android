package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.helper.extension.traceFilterClear

public open class FilterClearViewModel {

    init {
        traceFilterClear()
    }

    public val eventClear: SubscriptionEvent<Unit> = SubscriptionEvent()
}
