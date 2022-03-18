package com.algolia.instantsearch.stats

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceStats
import com.algolia.search.model.response.ResponseSearch

public open class StatsViewModel(response: ResponseSearch? = null) {

    public val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(response)

    init {
        traceStats()
    }
}
