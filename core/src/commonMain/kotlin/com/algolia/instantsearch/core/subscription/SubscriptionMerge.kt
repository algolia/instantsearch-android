package com.algolia.instantsearch.core.subscription


/**
 * Merges two [Subscriptions][Subscription],
 *
 * @param initialValue the new Subscription's initial value.
 * @param subscriptionA the first Subscription to merge.
 * @param subscriptionB the second Subscription to merge.
 * @param merge a function to transform both values from A and B into the expected [T].
 *
 * @return a [Subscription]<[T]> with the [merge]d values.
 */
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