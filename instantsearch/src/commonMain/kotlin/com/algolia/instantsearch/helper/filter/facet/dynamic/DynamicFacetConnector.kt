package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherIndex
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
public class DynamicFacetConnector(
    public val searcher: SearcherIndex<*>,
    public val filterState: FilterState,
    public val viewModel: DynamicFacetViewModel = DynamicFacetViewModel(),
    filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor> = emptyMap()
) : ConnectionImpl() {

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
