package com.algolia.instantsearch.filter.facet

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.extension.traceFacetListConnector
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.SearcherForFacets
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * RefinementList is a filtering view that displays facets, and lets the user refine their search results by filtering
 * on specific values.
 * Requirements: The attribute provided to the widget must be added in attributes for faceting, either on the dashboard
 * or using attributesForFaceting with the API.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/refinement-list/android/)
 */
public data class FacetListConnector internal constructor(
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val viewModel: FacetListViewModel,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
    private val wrapper: Wrapper,
) : ConnectionImpl() {

    internal sealed class Wrapper {

        class Single(val searcher: SearcherForHits<*>) : Wrapper()

        class ForFacet(val searcher: SearcherForFacets<*>) : Wrapper()
    }

    init {
        traceFacetListConnector()
    }

    private val connectionSearcher = when (wrapper) {
        is Wrapper.Single -> viewModel.connectSearcher(wrapper.searcher, attribute)
        is Wrapper.ForFacet -> viewModel.connectSearcherForFacet(wrapper.searcher)
    }
    private val connectionFilterState = viewModel.connectFilterState(filterState, attribute, groupID)

    public constructor(
        searcher: SearcherForHits<*>,
        filterState: FilterState,
        attribute: Attribute,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
        viewModel: FacetListViewModel = FacetListViewModel(),
    ) : this(filterState, attribute, viewModel, groupID, Wrapper.Single(searcher))

    public constructor(
        searcher: SearcherForFacets<*>,
        filterState: FilterState,
        attribute: Attribute,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
        viewModel: FacetListViewModel = FacetListViewModel(),
    ) : this(filterState, searcher.attribute, viewModel, groupID, Wrapper.ForFacet(searcher))

    public constructor(
        searcher: SearcherForHits<*>,
        filterState: FilterState,
        attribute: Attribute,
        selectionMode: SelectionMode = SelectionMode.Multiple,
        items: List<Facet> = listOf(),
        persistentSelection: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
    ) : this(
        filterState,
        attribute,
        FacetListViewModel(items, selectionMode, persistentSelection),
        groupID,
        Wrapper.Single(searcher)
    )

    public constructor(
        searcher: SearcherForFacets<*>,
        filterState: FilterState,
        attribute: Attribute,
        selectionMode: SelectionMode = SelectionMode.Multiple,
        items: List<Facet> = listOf(),
        persistentSelection: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or),
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
