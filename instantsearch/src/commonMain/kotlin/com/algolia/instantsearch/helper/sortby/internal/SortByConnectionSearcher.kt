package com.algolia.instantsearch.helper.sortby.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.SortByViewModel

/**
 * Sort by widget connection to a searcher.
 */
internal data class SortByConnectionSearcher<S>(
    private val viewModel: SortByViewModel,
    private val searcher: S,
) : ConnectionImpl() where S : Searcher<*>, S : IndexNameHolder {

    init {
        viewModel.updateSelection()
    }

    private val updateIndex: Callback<Int?> = { selection ->
        viewModel.updateSelection(selection)
        searcher.searchAsync()
    }

    override fun connect() {
        super.connect()
        viewModel.eventSelection.subscribe(updateIndex)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.eventSelection.unsubscribe(updateIndex)
    }

    private fun SortByViewModel.updateSelection(selection: Int? = selected.value) {
        map.value[selection]?.let { searcher.indexName = it }
    }
}
