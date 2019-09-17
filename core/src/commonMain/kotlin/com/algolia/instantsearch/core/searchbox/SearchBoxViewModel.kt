package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField


public open class SearchBoxViewModel {

    @JvmField
    public val query = SubscriptionValue<String?>(null)
    @JvmField
    public val eventSubmit = SubscriptionEvent<String?>()
}