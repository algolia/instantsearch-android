package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue


public open class LoadingViewModel(
    isLoading: Boolean = false
) {

    public val isLoading = SubscriptionValue(isLoading)
    public val eventReload = SubscriptionEvent<Unit>()
}