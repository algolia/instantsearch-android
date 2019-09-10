package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField


public open class LoadingViewModel(
    isLoading: Boolean = false
) {

    @JvmField
    public val isLoading = SubscriptionValue(isLoading)
    @JvmField
    public val eventReload = SubscriptionEvent<Unit>()
}