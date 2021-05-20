package com.algolia.instantsearch.core.internal

import java.util.concurrent.CopyOnWriteArraySet

internal actual fun <T> frozenCopyOnWriteSet(collection: Collection<T>?): MutableSet<T> {
    return if (collection == null) {
        CopyOnWriteArraySet()
    } else {
        CopyOnWriteArraySet(collection)
    }
}
