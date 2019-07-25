package com.algolia.instantsearch.helper.searcher

import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.search.model.response.ResponseSearch


internal class SearcherConnectionListAdapter<T>(
    private val searcher: SearcherSingleIndex,
    private val adapter: ListAdapter<T, *>,
    private val transform: (List<ResponseSearch.Hit>) -> List<T>
) : ConnectionImpl() {

    private val callback: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            adapter.submitList(transform(response.hits))
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}