package com.algolia.instantsearch.helper.sortby.searcher.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.searcher.SortByConnector
import com.algolia.instantsearch.helper.sortby.searcher.SortByViewModel
import com.algolia.instantsearch.helper.sortby.searcher.connectSearcher

/**
 * Default Sort By widget implementation.
 */
internal class DefaultSortByConnector<S>(
    override val searcher: S,
    override val viewModel: SortByViewModel
) : ConnectionImpl(), SortByConnector<S> where S : Searcher<*>, S : IndexNameHolder {

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
