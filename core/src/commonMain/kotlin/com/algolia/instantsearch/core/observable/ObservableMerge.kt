package com.algolia.instantsearch.core.observable


public fun <R, S, T> observableMerge(
    initialValue: T,
    source1: ObservableItem<R>,
    source2: ObservableItem<S>,
    merge: (R, S) -> T
): ObservableItem<T> {
    return ObservableItem(initialValue).apply {
        source1.subscribePast { value = merge(it, source2.value) }
        source2.subscribePast { value = merge(source1.value, it) }
    }
}