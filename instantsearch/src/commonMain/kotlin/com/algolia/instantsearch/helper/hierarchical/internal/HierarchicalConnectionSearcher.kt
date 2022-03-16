package com.algolia.instantsearch.helper.hierarchical.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet

internal data class HierarchicalConnectionSearcher(
    private val viewModel: HierarchicalViewModel,
    private val searcher: SearcherForHits<*>,
) : ConnectionImpl() {

    private val updateTree: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            val facets = response.hierarchicalFacetsOrNull
                ?: response.facetsOrNull?.filter { it.key == viewModel.hierarchicalAttributes.first() } ?: mapOf()

            viewModel.tree.value = viewModel.hierarchicalAttributes
                .mapNotNull { facets[it]?.toMutableList() }
                .filterUnprefixed()
                .flatten()
                .toNodes(selectedHierarchicalValue)
        }
    }

    /**
     * Removes results not matching the naming pattern.
     * This is a workaround to remove unexpected categories in results.
     *
     * Let's consider an item with the following filters:
     * Level 0: [Clothing, Top]
     * Level 1: [Clothing > Men, Clothing > Women, Top > T-shirts]
     *
     * In case of selecting 'Clothing' the tree will be:
     * Level 0: [Clothing, Furniture]
     * Level 1: [Clothing > Men, Clothing > Women]
     */
    private fun List<MutableList<Facet>>.filterUnprefixed(): List<MutableList<Facet>> {
        viewModel.hierarchicalPath.value.forEachIndexed { index, (_, item) ->
            getOrNull(index + 1) // Get next level (sub-category)
                ?.removeAll { !it.value.startsWith(item) } // Remove the items not respecting the prefix convention
        }
        return this
    }

    private val selectedHierarchicalValue: String?
        get() {
            val value = viewModel.selections.value
            if (value.isEmpty()) return null
            return value.joinToString(viewModel.separator)
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
