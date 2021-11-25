package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.telemetry.ComponentType.Loading

public open class LoadingViewModel(
    isLoading: Boolean = false
) {

    init {
        GlobalTelemetry.traceViewModel(Loading)
    }

    public val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(isLoading)
    public val eventReload: SubscriptionEvent<Unit> = SubscriptionEvent()
}
