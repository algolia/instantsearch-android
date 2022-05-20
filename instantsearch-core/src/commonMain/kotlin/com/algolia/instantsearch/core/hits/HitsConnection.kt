package com.algolia.instantsearch.core.hits

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.hits.internal.HitsConnectionView
import com.algolia.instantsearch.core.searcher.Searcher

/**
 * Connects [Searcher] to a [HitsView].
 *
 * @param adapter hits view adapter
 * @param past subscribe to past result
 * @param presenter hits presenter
 */
public fun <R, T> Searcher<R>.connectHitsView(
    adapter: HitsView<T>,
    past: Boolean = false,
    presenter: Presenter<R, List<T>>
): Connection {
    return HitsConnectionView(this, adapter, presenter, past)
}
