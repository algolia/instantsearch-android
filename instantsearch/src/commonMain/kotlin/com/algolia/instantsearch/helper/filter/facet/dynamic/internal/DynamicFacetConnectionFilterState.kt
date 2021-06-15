package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

/**
 * Connection between a dynamic facets business logic and a filter state.
 *
 * @param viewModel Dynamic facets business logic
 * @param filterState FilterState that holds your filters
 * @param groupIDForAttribute Mapping between a facet attribute and a filter group where corresponding facet filters stored in the filter state
 */
internal class DynamicFacetConnectionFilterState(
    val viewModel: DynamicFacetViewModel,
    val filterState: FilterState,
    val groupIDForAttribute: Map<Attribute, FilterGroupID>
) : ConnectionImpl() {

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
            .map { it.value.toString() }
            .toSet()
    }

    private val onSelectionsComputedSubscription: Callback<SelectionsPerAttribute> = { selectionsPerAttribute ->
        selectionsPerAttribute.onEach { (attribute, selections) ->
            val groupID = groupID(attribute)
            val filters = selections.map { Filter.Facet(attribute, it) }
            filterState.remove(attribute) // TODO: groupID + attribute
            filterState.add(groupID, *filters.toTypedArray())
        }
        filterState.notifyChange()
    }

    private fun groupID(attribute: Attribute): FilterGroupID {
        return groupIDForAttribute[attribute] ?: FilterGroupID(attribute = attribute, operator = FilterOperator.And)
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
