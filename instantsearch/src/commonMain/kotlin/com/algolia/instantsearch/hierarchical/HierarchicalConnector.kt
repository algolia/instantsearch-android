package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.extension.traceHierarchicalFacetsConnector
import com.algolia.instantsearch.filter.state.FilterState

import com.algolia.instantsearch.searcher.SearcherForHits

/**
 * Hierarchical Menu is a filtering view that displays a hierarchy of facets which lets the user refine
 * the search results.
 * [Documentation](https//www.algolia.com/doc/api-reference/widgets/hierarchical-menu/android/)
 *
 * @param searcher the Searcher that handles your searches
 * @param filterState the FilterState that will hold your filters
 * @param viewModel the logic applied to the hierarchical facets
 */
public data class HierarchicalConnector(
    public val searcher: SearcherForHits<*>,
    public val filterState: FilterState,
    public val viewModel: HierarchicalViewModel,
) : AbstractConnection() {

    /**
     * @param searcher the Searcher that handles your searches
     * @param attribute the attribute to filter.
     * @param filterState the FilterState that will hold your filters
     * @param hierarchicalAttributes the names of the hierarchical attributes that we need to target, in ascending order
     * @param separator The string separating the facets in the hierarchical facets. Usually something like " > "
     */
    public constructor(
        searcher: SearcherForHits<*>,
        attribute: String,
        filterState: FilterState,
        hierarchicalAttributes: List<Attribute>,
        separator: String,
    ) : this(searcher, filterState, HierarchicalViewModel(attribute, hierarchicalAttributes, separator))

    init {
        traceHierarchicalFacetsConnector()
    }

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
