package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


internal class IndexSegmentConnectionSearcher(
    private val viewModel: IndexSegmentViewModel,
    private val searcher: SearcherSingleIndex
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

    private fun IndexSegmentViewModel.updateSelection(selection: Int? = selected.value) {
        map.value[selection]?.let { searcher.index = it }
    }
}