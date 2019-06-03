package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


public inline fun <reified T : Number> NumberViewModel<T>.connectFilterState(
    attribute: Attribute,
    operator : NumericOperator,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
) {
    onNumberComputed += { computed ->
        filterState.notify {
            item?.let { remove(groupID, Filter.Numeric(attribute, operator, it)) }
            computed?.let { add(groupID, Filter.Numeric(attribute, operator, it)) }
        }
    }

    val onChanged: (Filters) -> Unit = { filters ->
        item = filters
            .getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Comparison>()
            .firstOrNull { it.operator == operator }
            ?.number as? T?
    }

    onChanged(filterState)
    filterState.onChanged += onChanged
}