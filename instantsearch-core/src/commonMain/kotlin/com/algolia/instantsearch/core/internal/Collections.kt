package com.algolia.instantsearch.core.internal

/**
 * Creates a copy-on-write list.
 */
internal expect fun <T> frozenCopyOnWriteSet(collection: Collection<T>? = null): MutableSet<T>
