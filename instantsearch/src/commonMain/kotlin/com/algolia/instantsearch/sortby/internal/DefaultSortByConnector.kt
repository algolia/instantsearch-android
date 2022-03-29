package com.algolia.instantsearch.sortby.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.extension.traceSortByConnector
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.sortby.SortByConnector
import com.algolia.instantsearch.sortby.SortByViewModel
import com.algolia.instantsearch.sortby.connectSearcher

/**
 * Default Sort By widget implementation.
 */
internal class DefaultSortByConnector<S>(
    override val searcher: S,
    override val viewModel: SortByViewModel
) : ConnectionImpl(), SortByConnector<S> where S : Searcher<*>, S : IndexNameHolder {

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    init {
        traceSortByConnector()
    }

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
