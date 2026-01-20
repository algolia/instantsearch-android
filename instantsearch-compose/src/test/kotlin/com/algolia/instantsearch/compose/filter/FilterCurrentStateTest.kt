package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.state.FilterGroupID
import kotlin.test.Test
import kotlin.test.assertEquals

public class FilterCurrentStateTest {

    private val initFilters = emptyList<Pair<FilterAndID, String>>()
    private val filterCurrentState = FilterCurrentState(initFilters)
    private val filterGroupID = FilterGroupID("os")
    private val facet = Filter.Facet("os", "android")

    @Test
    public fun testFilters() {
        assertEquals(initFilters, filterCurrentState.filters)
        Snapshot.takeSnapshot {
            val filters = listOf(filterGroupID to facet to "1")
            filterCurrentState.setFilters(filters)
            assertEquals(filters, filterCurrentState.filters)
        }
        assertEquals(initFilters, filterCurrentState.filters)
    }

    @Test
    public fun testSelect() {
        var selected: FilterGroupID? = null
        filterCurrentState.onFilterSelected = { selected = it.first }
        filterCurrentState.selectFilter(filterGroupID to facet)
        assertEquals(filterGroupID, selected)
    }
}
