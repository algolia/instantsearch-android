package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.model.response.ResponseSearch
import kotlin.jvm.JvmField


/**
 * A ViewModel storing a [search response][ResponseSearch] to present associated stats.
 */
public open class StatsViewModel(response: ResponseSearch? = null) {
    @JvmField
    val response = SubscriptionValue(response)
}