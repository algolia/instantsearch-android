package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.internal.traceLoading
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue

public class LoadingViewModel(
    isLoading: Boolean = false
) {
    init {
        traceLoading(isLoading)
    }

    public val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(isLoading)
    public val eventReload: SubscriptionEvent<Unit> = SubscriptionEvent()
}
