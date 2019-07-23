package com.algolia.instantsearch.core.observable


public fun <R, S, T> mergeSubscription(
    initialValue: T,
    subscriptionA: SubscriptionValue<R>,
    subscriptionB: SubscriptionValue<S>,
    merge: (R, S) -> T
): SubscriptionValue<T> {
    return SubscriptionValue(initialValue).apply {
        subscriptionA.subscribePast { value = merge(it, subscriptionB.value) }
        subscriptionB.subscribePast { value = merge(subscriptionA.value, it) }
    }
}