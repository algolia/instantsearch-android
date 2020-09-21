package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

internal data class SortByConnectionSearcher(
    private val viewModel: SortByViewModel,
    private val searcher: SearcherSingleIndex,
) : ConnectionImpl() {

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
        map.value[selection]?.let { searcher.index = it }
    }
}
