package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.extension.traceDynamicFacetConnector
import com.algolia.instantsearch.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.Attribute

/**
 * Component to display ordered facets automatically, their ordered values, and lets the user refine the search results
 * by filtering on specific values.
 *
 * @param searcher searcher handling searches
 * @param filterState filterState holding filters
 * @param viewModel dynamic facets business logic
 * @param filterGroupForAttribute mapping between a facet attribute and a descriptor of a filter group where the
 * corresponding facet filters stored in the filter state. If no filter group descriptor provided, the filters for
 * attribute will be automatically stored in the conjunctive (`and`) group with the facet attribute name.
 */
public class DynamicFacetListConnector(
    public val searcher: SearcherForHits<*>,
    public val filterState: FilterState,
    public val viewModel: DynamicFacetListViewModel = DynamicFacetListViewModel(),
    filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor> = emptyMap()
) : AbstractConnection() {

    /**
     * @param searcher searcher handling searches
     * @param filterState filterState holding filters
     * @param orderedFacets ordered list of attributed facets
     * @param selections mapping between a facet attribute and a set of selected facet values
     * @param selectionModeForAttribute Mapping between a facet attribute and a facet values selection mode
     * @param filterGroupForAttribute mapping between a facet attribute and a descriptor of a filter group where the
     * corresponding facet filters stored in the filter state. If no filter group descriptor provided, the filters for
     * attribute will be automatically stored in the conjunctive (`and`) group with the facet attribute name.
     */
    public constructor(
        searcher: SearcherForHits<*>,
        filterState: FilterState,
        orderedFacets: List<AttributedFacets> = emptyList(),
        selections: SelectionsPerAttribute = mutableMapOf(),
        selectionModeForAttribute: Map<Attribute, SelectionMode> = emptyMap(),
        filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor> = emptyMap()
    ) : this(searcher, filterState, DynamicFacetListViewModel(orderedFacets, selections, selectionModeForAttribute), filterGroupForAttribute)

    init {
        traceDynamicFacetConnector(filterGroupForAttribute)
    }

    private val searcherConnection = viewModel.connectSearcher(searcher)
    private val filterStateConnection = viewModel.connectFilterState(filterState, filterGroupForAttribute)

    override fun connect() {
        super.connect()
        searcherConnection.connect()
        filterStateConnection.connect()
    }

    override fun disconnect() {
        super.disconnect()
        searcherConnection.disconnect()
        filterStateConnection.disconnect()
    }
}
