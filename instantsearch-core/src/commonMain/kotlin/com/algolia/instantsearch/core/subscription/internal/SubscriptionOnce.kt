package com.algolia.instantsearch.core.subscription.internal

import com.algolia.instantsearch.core.subscription.Subscription

internal class SubscriptionOnce<T>(
    private val subscription: Subscription<T>,
    private val skipNull: Boolean,
    private val call: (T) -> Unit
) : (T) -> Unit {

    override fun invoke(value: T) {
        if (value == null && skipNull) return
        execute(value)
    }

    private fun execute(value: T) {
        subscription.unsubscribe(this)
        call(value)
    }
}
