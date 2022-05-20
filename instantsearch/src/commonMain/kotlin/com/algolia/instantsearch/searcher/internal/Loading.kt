package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.subscription.SubscriptionValue

internal typealias LoadingSubscription = SubscriptionValue<Boolean>

/** Sets loading status while executing [block] */
internal suspend inline fun LoadingSubscription.runAsLoading(crossinline block: suspend () -> Unit) {
    value = true
    block()
    value = false
}
