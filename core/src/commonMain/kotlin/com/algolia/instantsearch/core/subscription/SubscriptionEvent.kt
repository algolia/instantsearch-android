package com.algolia.instantsearch.core.subscription


/**
 * An event that can be triggered, sending its value to all registered callbacks.
 */
public class SubscriptionEvent<T> : Subscription<T>() {

    /**
     * Sends an event to every subscriber.
     *
     * @param event the value to forward to callbacks.
     */
    public fun send(event: T) {
        subscriptions.forEach { it(event) }
    }
}

/**
 * Sends an event with no data to every subscriber.
 */
public fun SubscriptionEvent<Unit>.send() = send(Unit)