package com.algolia.instantsearch.stats

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceStats

public open class StatsViewModel(response: SearchResponse? = null) {

    public val response: SubscriptionValue<SearchResponse?> = SubscriptionValue(response)

    init {
        traceStats()
    }
}
