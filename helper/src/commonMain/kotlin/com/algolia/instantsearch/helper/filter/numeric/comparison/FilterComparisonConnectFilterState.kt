package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


public inline fun <reified T : Number> SelectableNumberViewModel<T>.connectFilterState(
    attribute: Attribute,
    operator : NumericOperator,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.And(attribute)
) {
    onNumberComputed += { computed ->
        filterState.notify {
            number?.let { remove(groupID, Filter.Numeric(attribute, operator, it)) }
            computed?.let { add(groupID, Filter.Numeric(attribute, operator, it)) }
        }
    }

    val onChange: (Filters) -> Unit = { filters ->
        number = filters
            .getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Comparison>()
            .firstOrNull { it.operator == operator }
            ?.number as? T?
    }

    onChange(filterState)
    filterState.onChange += onChange
}