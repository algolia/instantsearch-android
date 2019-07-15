package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


public inline fun <reified T> NumberViewModel<T>.connectFilterState(
    attribute: Attribute,
    operator: NumericOperator,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
    key: ObservableKey? = null
) where T : Number, T : Comparable<T> {
    filterState.filters.subscribePast(key) { filters ->
        number.set(filters
            .getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Comparison>()
            .firstOrNull { it.operator == operator }
            ?.number as? T?)
    }
    event.subscribe(key) { computed ->
        filterState.notify {
            number.get()?.let { remove(groupID, Filter.Numeric(attribute, operator, it)) }
            computed?.let { add(groupID, Filter.Numeric(attribute, operator, it)) }
        }
    }
}