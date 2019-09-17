@file:JvmName("Hits")

package com.algolia.instantsearch.core.hits


import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import kotlin.jvm.JvmName


/**
 * Connects this Searcher to a HitsView, updating its data on new search responses.
 */
public fun <R, T> Searcher<R>.connectHitsView(
    adapter: HitsView<T>,
    transform: (R) -> List<T>
): Connection {
    return HitsConnectionView(this, adapter, transform)
}