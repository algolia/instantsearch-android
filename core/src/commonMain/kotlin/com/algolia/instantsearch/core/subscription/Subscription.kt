@file:JvmName("Subscription")

package com.algolia.instantsearch.core.subscription

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.CallbackImpl
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic


//TODO DISCUSS: Naming could be improved, subscription.subscriptions makes no sense.
//-> Subscription.callbacks / subscription.subscribe(callback)?
/**
 * A Subscription, letting you subcribe several callbacks that can be triggered.
 */
public open class Subscription<T> {

    //TODO DISCUSS: rename to callbacks
    internal val subscriptions: MutableSet<Callback<T>> = mutableSetOf()
        @JvmSynthetic get

    /**
     * Registers a callback to this Subscription.
     *
     * @param subscription the callback to execute.
     */
    @JvmSynthetic
    public fun subscribe(subscription: (T) -> Unit) {
        subscriptions += CallbackImpl(subscription)
    }

    /**
     * Unregisters a callback to this Subscription.
     *
     * @param subscription a previously subscribed callback.
     */
    @JvmSynthetic
    public fun unsubscribe(subscription: (T) -> Unit) {
        subscriptions -= CallbackImpl(subscription)
    }

    /**
     * Unregisters any callback registered.
     */
    public fun unsubscribeAll() {
        subscriptions.clear()
    }

    /**
     * Registers a callback to this Subscription.
     *
     * @param callback the callback to execute.
     */
    public fun subscribe(callback: Callback<T>) {
        subscriptions += callback
    }

    /**
     * Unregisters a callback to this Subscription.
     *
     * @param callback a previously subscribed callback.
     */
    public fun unsubscribe(callback: Callback<T>) {
        subscriptions -= callback
    }
}