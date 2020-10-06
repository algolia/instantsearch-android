package com.algolia.instantsearch.core.searchbox.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel

internal data class SearchBoxConnectionView(
    private val viewModel: SearchBoxViewModel,
    private val view: SearchBoxView
) : ConnectionImpl() {

    override fun connect() {
        super.connect()
        view.setText(viewModel.query.value)
        view.onQueryChanged = (viewModel.query::value::set)
        view.onQuerySubmitted = {
            viewModel.query.value = it
            viewModel.eventSubmit.send(it)
        }
    }

    override fun disconnect() {
        super.disconnect()
        view.onQueryChanged = null
        view.onQuerySubmitted = null
    }
}
