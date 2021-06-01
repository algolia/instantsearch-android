package com.algolia.instantsearch.compose.filter.current.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.current.FilterAndID

/**
 * Implementation of [FilterCurrentState].
 *
 * @param filtersList initial value
 */
internal class FilterCurrentStateImpl(
    filtersList: List<Pair<FilterAndID, String>>
) : FilterCurrentState {

    override var filtersList: List<Pair<FilterAndID, String>> by mutableStateOf(filtersList)

    override var onFilterSelected: Callback<FilterAndID>? = null

    override fun setFilters(filters: List<Pair<FilterAndID, String>>) {
        this.filtersList = filters
    }

    override fun filterSelected(filterAndID: FilterAndID) {
        onFilterSelected?.invoke(filterAndID)
    }
}
