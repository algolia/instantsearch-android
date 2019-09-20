@file:JvmName("SortBy")

package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.index.*
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this SortByViewModel to a SortByView, updating it when the index changes.
 */
@JvmOverloads
public fun SortByViewModel.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return connectView(view, presenter)
}

/**
 * Connects this SortByViewModel to a SearcherSingleIndex, updating its index when the index changes.
 */
public fun SortByViewModel.connectSearcher(
    searcher: SearcherSingleIndex
): Connection {
    return SortByConnectionSearcher(this, searcher)
}

/**
 * Connects this SortByConnector to a SortByView, updating it when the index changes.
 */
@JvmOverloads
public fun SortByConnector.connectView(
    view: SortByView,
    presenter: IndexPresenter = IndexPresenterImpl
): Connection {
    return viewModel.connectView(view, presenter)
}