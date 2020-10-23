package com.algolia.instantsearch.ext.internal

import kotlinx.coroutines.channels.SendChannel

/**
 * [SendChannel.offer] that returns `false` when this [SendChannel.isClosedForSend], instead of throwing.
 */
internal fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
    return runCatching { offer(element) }.getOrDefault(false)
}
