package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterNumeric
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public inline fun <reified T> FilterRangeViewModel<T>.connectFilterState(
    attribute: Attribute,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(attribute.raw),
    key: ObservableKey? = null
) where T : Number, T : Comparable<T> {
    filterState.filters.subscribePast(key) { filters ->
        val filter = filters.getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Range>()
            .firstOrNull()

        range.set(if (filter != null) Range(filter.lowerBound as T, filter.upperBound as T) else null)
    }
    event.subscribe(key) { range ->
        filterState.notify {
            this@connectFilterState.range.get()?.let { remove(groupID, it.toFilterNumeric(attribute)) }
            if (range != null) add(groupID, range.toFilterNumeric(attribute))
        }
    }
}