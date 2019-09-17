@file:JvmName("Hits")

package com.algolia.instantsearch.core.hits


import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import kotlin.jvm.JvmName


public fun <R, T> Searcher<R>.connectHitsView(
    adapter: HitsView<T>,
    transform: (R) -> List<T>
): Connection {
    return HitsConnectionView(this, adapter, transform)
}