package com.algolia.instantsearch.compose.filter.current.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.filter.current.FilterAndID

/**
 * Implementation of [FilterCurrentState].
 *
 * @param filters initial value
 */
internal class FilterCurrentStateImpl(
    filters: List<Pair<FilterAndID, String>>
) : FilterCurrentState {

    @set:JvmName("_filters")
    override var filters: List<Pair<FilterAndID, String>> by mutableStateOf(filters)

    override var onFilterSelected: Callback<FilterAndID>? = null

    init {
        trace()
    }

    override fun setFilters(filters: List<Pair<FilterAndID, String>>) {
        this.filters = filters
    }

    override fun selectFilter(filterAndID: FilterAndID) {
        onFilterSelected?.invoke(filterAndID)
    }
}
