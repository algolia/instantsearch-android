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
                item = createRange(it.value as Filter.Numeric.Value.Range)
            }
    }
    onChanged(filterState)
    filterState.onChanged += onChanged

    onRangeComputed += {
        filterState.notify { createNumericFilter(attribute, it)?.let { add(groupID, it) } }
    }
}

private fun <T : Number> createRange(value: Filter.Numeric.Value.Range): Range<T> {
    return when (value.lowerBound) {
        is Int -> Range.Int(value.lowerBound as Int..value.upperBound as Int)
        is Long -> Range.Long(value.lowerBound as Long..value.upperBound as Long)
        is Float -> Range.Float(value.lowerBound as Float..value.upperBound as Float)
        is Double -> Range.Double(value.lowerBound as Double..value.upperBound as Double)
        else -> throw IllegalStateException("$value bounds don't match a supported Number subclass")
    } as Range<T>
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