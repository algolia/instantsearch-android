package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue


public open class SearchBoxViewModel {

    public val query: SubscriptionValue<String?> = SubscriptionValue(null)
    public val eventSubmit: SubscriptionEvent<String?> = SubscriptionEvent()
}
