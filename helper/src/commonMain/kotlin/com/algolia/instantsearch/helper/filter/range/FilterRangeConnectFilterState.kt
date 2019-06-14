package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

public fun <T : Number> NumberRangeViewModel<T>.connectFilterState(
    attribute: Attribute,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(attribute.raw)
) {
    val onChanged: (Filters) -> Unit = { filters ->
        filters.getNumericFilters(groupID)
            .firstOrNull { it.value is Filter.Numeric.Value.Range }
            ?.let {
                val rangeValue = it.value as Filter.Numeric.Value.Range
                item = when (rangeValue.lowerBound) {
                    is Int -> createRange(it)
//            is Long -> Range.Long(rangeValue.lowerBound, rangeValue.upperBound)
//            is Float -> Range.Float(rangeValue.lowerBound, rangeValue.upperBound)
//            is Double -> Range.Double(rangeValue.lowerBound, rangeValue.upperBound)
                    else -> TODO()
                } as Range<T>?
            }
    }
    onChanged(filterState)
    filterState.onChanged += onChanged

    onRangeComputed += {
        filterState.notify { createNumericFilter(attribute, it)?.let { add(groupID, it) } }
    }
}

fun createRange(filter: Filter.Numeric): Range<Int> {
    (filter.value as Filter.Numeric.Value.Range).let {
        return Range.Int(IntRange(it.lowerBound as Int, it.upperBound as Int))
    }
}

//fun <T : Number> createRange(filter: Filter.Numeric): Range<T> {
//
//}

private fun <T : Number> createNumericFilter(
    attribute: Attribute,
    range: Range<T>?
): Filter.Numeric? {
    return range?.let {
        when (range.min) {
            is Int -> Filter.Numeric(attribute, IntRange(range.min as Int, range.max as Int))
            is Long -> Filter.Numeric(attribute, LongRange(range.min as Long, range.max as Long))
            is Float -> Filter.Numeric(attribute, range.min as Float, range.max as Float)
            is Double -> Filter.Numeric(attribute, range.min as Double, range.max as Double)
            else -> throw IllegalStateException("$range doesn't match a supported Number subclass")
        }
    }
}