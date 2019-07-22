package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


internal class FilterRangeConnectionFilterState<T>(
    private val viewModel: FilterRangeViewModel<T>,
    private val filterState: FilterState,
    private val attribute: Attribute,
    private val groupID: FilterGroupID
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    @Suppress("UNCHECKED_CAST")
    private val updateRange: Callback<Filters> = { filters ->
        val filter = filters.getNumericFilters(groupID)
            .filter { it.attribute == attribute }
            .map { it.value }
            .filterIsInstance<Filter.Numeric.Value.Range>()
            .firstOrNull()

        viewModel.range.value = if (filter != null) Range(filter.lowerBound as T, filter.upperBound as T) else null
    }
    private val updateFilterState: Callback<Range<T>?> = { range ->
        filterState.notify {
            viewModel.range.value?.let { remove(groupID, it.toFilterNumeric(attribute)) }
            if (range != null) add(groupID, range.toFilterNumeric(attribute))
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribePast(updateRange)
        viewModel.eventRange.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateRange)
        viewModel.eventRange.unsubscribe(updateFilterState)
    }
}