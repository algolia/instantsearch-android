package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import com.algolia.instantsearch.core.observable.SubscriptionValue


public open class SearchBoxViewModel {

    public val query = SubscriptionValue<String?>(null)
    public val eventSubmit = SubscriptionEvent<String?>()
}