package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.FacetSelections
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter

internal class DynamicFacetConnectionFilterState(
    val viewModel: DynamicFacetViewModel,
    val filterState: FilterState,
) : ConnectionImpl() {

    private val callback: Callback<Filters> = { filters ->
        val attributesWithFacets = filters.getFacetGroups()
            .flatMap { it.value }
            .filterNot { it.isNegated }
            .groupBy({ it.attribute }, { it.value.asString() })
            .mapValues { it.value.toSet() }
            .toMutableMap()
        viewModel.selections = attributesWithFacets
    }

    private val selectionsSubscription: Callback<FacetSelections> = { selections ->
        selections.onEach { (attribute, selectionsSet) ->
            selectionsSet.onEach { selection ->
                filterState.toggle(FilterGroupID(attribute), Filter.Facet(attribute, selection))
            }
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribe(callback)
        selectionsUpdatedSubscribePast()
    }

    private fun selectionsUpdatedSubscribePast() {
        selectionsSubscription.invoke(viewModel.selections)
        viewModel.onSelectionsUpdated.subscribe(selectionsSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(callback)
        viewModel.onSelectionsUpdated.unsubscribe(selectionsSubscription)
    }
}
