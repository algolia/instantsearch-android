package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet


public data class FacetListConnector private constructor(
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val viewModel: FacetListViewModel,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
    private val wrapper: Wrapper
) : ConnectionImpl() {

    private sealed class Wrapper {

        class Single(val searcher: SearcherSingleIndex) : Wrapper()

        class ForFacet(val searcher: SearcherForFacets) : Wrapper()
    }

    private val connectionSearcher = when (wrapper) {
        is Wrapper.Single -> viewModel.connectSearcher(wrapper.searcher, attribute)
        is Wrapper.ForFacet -> viewModel.connectSearcherForFacet(wrapper.searcher)
    }
    private val connectionFilterState = viewModel.connectFilterState(filterState, attribute, groupID)

    constructor(
        searcher: SearcherSingleIndex,
        filterState: FilterState,
        attribute: Attribute,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
        viewModel: FacetListViewModel = FacetListViewModel()
    ) : this(filterState, attribute, viewModel, groupID, Wrapper.Single(searcher))

    constructor(
        searcher: SearcherForFacets,
        filterState: FilterState,
        attribute: Attribute,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
        viewModel: FacetListViewModel = FacetListViewModel()
    ) : this(filterState, attribute, viewModel, groupID, Wrapper.ForFacet(searcher))

    constructor(
        searcher: SearcherSingleIndex,
        filterState: FilterState,
        attribute: Attribute,
        selectionMode: SelectionMode = SelectionMode.Multiple,
        items: List<Facet> = listOf(),
        persistentSelection: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or)
    ) : this(
        filterState,
        attribute,
        FacetListViewModel(items, selectionMode, persistentSelection),
        groupID,
        Wrapper.Single(searcher)
    )

    constructor(
        searcher: SearcherForFacets,
        filterState: FilterState,
        attribute: Attribute,
        selectionMode: SelectionMode = SelectionMode.Multiple,
        items: List<Facet> = listOf(),
        persistentSelection: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or)
    ) : this(
        filterState,
        attribute,
        FacetListViewModel(items, selectionMode, persistentSelection),
        groupID,
        Wrapper.ForFacet(searcher)
    )

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