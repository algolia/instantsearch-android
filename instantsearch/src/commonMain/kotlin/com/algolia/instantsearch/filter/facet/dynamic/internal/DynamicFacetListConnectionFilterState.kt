package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListViewModel
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

/**
 * Connection between a dynamic facets business logic and a filter state.
 *
 * @param viewModel dynamic facets business logic
 * @param filterState filterState that holds your filters
 * @param filterGroupForAttribute mapping between a facet attribute and a descriptor of a filter group where the
 * corresponding facet filters stored in the filter state. If no filter group descriptor provided, the filters for
 * attribute will be automatically stored in the group of `defaultFilterOperator` type with the facet attribute name.
 * @param defaultFilterOperator type of filter group created by default for a facet attribute
 */
internal class DynamicFacetListConnectionFilterState(
    val viewModel: DynamicFacetListViewModel,
    val filterState: FilterState,
    val filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor>,
    val defaultFilterOperator: FilterOperator,
) : AbstractConnection() {

    private val filterStateSubscription: Callback<Filters> = {
        val selectionsPerAttribute = viewModel.orderedFacets
            .asSequence()
            .map { it.attribute }
            .map { it to facetValuesOf(it) }
            .toMap()
        viewModel.selections = selectionsPerAttribute
    }

    private fun facetValuesOf(attribute: Attribute): Set<String> {
        return filterState.getFilters(groupID(attribute))
            .asSequence()
            .filterIsInstance<Filter.Facet>()
            .filter { it.attribute == attribute && !it.isNegated }
            .map { it.value.raw.toString() }
            .toSet()
    }

    private val onSelectionsComputedSubscription: Callback<SelectionsPerAttribute> = { selectionsPerAttribute ->
        selectionsPerAttribute.onEach { (attribute, selections) ->
            val groupID = groupID(attribute)
            val filters = selections.map { Filter.Facet(attribute, it) }.toTypedArray()
            filterState.clear(groupID)
            filterState.add(groupID, *filters)
        }
        filterState.notifyChange()
    }

    private fun groupID(attribute: Attribute): FilterGroupID {
        val (groupName, refinementOperator) = filterGroupForAttribute[attribute] ?: (attribute to defaultFilterOperator)
        return when (refinementOperator) {
            FilterOperator.And -> FilterGroupID(groupName, FilterOperator.And)
            FilterOperator.Or -> FilterGroupID(groupName, FilterOperator.Or)
        }
    }

    override fun connect() {
        super.connect()
        viewModel.onSelectionsComputed.subscribePast(onSelectionsComputedSubscription)
        filterState.filters.subscribePast(filterStateSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.onSelectionsComputed.unsubscribe(onSelectionsComputedSubscription)
        filterState.filters.unsubscribe(filterStateSubscription)
    }
}
