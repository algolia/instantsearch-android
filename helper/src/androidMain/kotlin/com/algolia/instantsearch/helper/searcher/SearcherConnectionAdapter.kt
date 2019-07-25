package com.algolia.instantsearch.helper.searcher

import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.search.model.response.ResponseSearch


public fun <T> SearcherSingleIndex.connectionListAdapter(
    adapter: ListAdapter<T, *>,
    transform: (List<ResponseSearch.Hit>) -> List<T>
): Connection {
    return SearcherConnectionListAdapter(this, adapter, transform)
}