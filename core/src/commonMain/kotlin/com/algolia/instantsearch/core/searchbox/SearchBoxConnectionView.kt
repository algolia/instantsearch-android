package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection


public class SearchBoxConnectionView(
    public val viewModel: SearchBoxViewModel,
    public val view: SearchBoxView
) : Connection {

    override var isConnected: Boolean = false

    override fun connect() {
        super.connect()
        view.setText(viewModel.query.get())
        view.onQueryChanged = (viewModel.query::set)
        view.onQuerySubmitted = {
            viewModel.query.set(it)
            viewModel.eventSubmit.send(it)
        }
    }

    override fun disconnect() {
        super.disconnect()
        view.onQueryChanged = null
        view.onQuerySubmitted = null
    }
}