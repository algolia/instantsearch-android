package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute

public data class HierarchicalConnector(
    public val searcher: SearcherSingleIndex,
    public val filterState: FilterState,
    public val viewModel: HierarchicalViewModel,
) : ConnectionImpl() {

    public constructor(
        searcher: SearcherSingleIndex,
        attribute: Attribute,
        filterState: FilterState,
        hierarchicalAttributes: List<Attribute>,
        separator: String,
    ) : this(searcher, filterState, HierarchicalViewModel(attribute, hierarchicalAttributes, separator))

    private val connectionSearcher = viewModel.connectSearcher(searcher)
    private val connectionFilterState = viewModel.connectFilterState(filterState)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
        connectionFilterState.disconnect()
    }
}
