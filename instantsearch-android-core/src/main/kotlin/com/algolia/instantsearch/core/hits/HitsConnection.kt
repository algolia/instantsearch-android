package com.algolia.instantsearch.core.hits

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher

public fun <R, T> Searcher<R>.connectHitsView(
    adapter: HitsView<T>,
    presenter: Presenter<R, List<T>>
): Connection {
    return HitsConnectionView(this, adapter, presenter)
}
