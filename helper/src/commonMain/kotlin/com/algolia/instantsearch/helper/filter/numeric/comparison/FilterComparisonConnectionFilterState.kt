package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


internal class FilterComparisonConnectionFilterState<T>(
    private val viewModel: NumberViewModel<T>,
    private val filterState: FilterState,
    private val attribute: Attribute,
    private val operator: NumericOperator,
    private val groupID: FilterGroupID
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    @Suppress("UNCHECKED_CAST")
    private val updateNumber: Callback<Filters> = { filters ->
        viewModel.number.value = filters
            .getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Comparison>()
            .firstOrNull { it.operator == operator }
            ?.number as? T?
    }
    private val updateFilterState: Callback<T?> = { number ->
        filterState.notify {
            viewModel.number.value?.let { remove(groupID, Filter.Numeric(attribute, operator, it)) }
            number?.let { add(groupID, Filter.Numeric(attribute, operator, it)) }
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribePast(updateNumber)
        viewModel.eventNumber.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateNumber)
        viewModel.eventNumber.unsubscribe(updateFilterState)
    }
}