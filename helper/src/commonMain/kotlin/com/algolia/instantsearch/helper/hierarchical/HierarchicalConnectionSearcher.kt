package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.response.ResponseSearch


internal data class HierarchicalConnectionSearcher(
    private val viewModel: HierarchicalViewModel,
    private val searcher: SearcherSingleIndex
) : ConnectionImpl() {

    private val updateTree: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            val facets = response.hierarchicalFacetsOrNull
                ?: response.facetsOrNull?.filter { it.key == viewModel.hierarchicalAttributes.first() } ?: mapOf()

            viewModel.tree.value = viewModel.hierarchicalAttributes.mapNotNull { facets[it] }.flatten().toNodes()
        }
    }

    init {
        searcher.query.addFacet(*viewModel.hierarchicalAttributes.toTypedArray())
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(updateTree)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(updateTree)
    }
}