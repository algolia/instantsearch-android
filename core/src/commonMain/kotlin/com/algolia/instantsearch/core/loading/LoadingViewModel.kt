package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import com.algolia.instantsearch.core.observable.SubscriptionValue


public open class LoadingViewModel(
    isLoading: Boolean = false
) {

    public val isLoading = SubscriptionValue(isLoading)
    public val eventReload = SubscriptionEvent<Unit>()
}