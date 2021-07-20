package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.filter.map.FilterMapState
import kotlin.test.Test
import kotlin.test.assertEquals

public class FilterMapStateTest {

    private val init = emptyMap<Int, String>()
    private val filterMapState = FilterMapState(init)

    @Test
    public fun testOptions() {
        assertEquals(init, filterMapState.options)
        Snapshot.takeSnapshot {
            val selected = mapOf(1 to "1")
            filterMapState.setMap(selected)
            assertEquals(selected, filterMapState.options)
        }
        assertEquals(init, filterMapState.options)
    }

    @Test
    public fun testSelected() {
        assertEquals(null, filterMapState.selected)
        Snapshot.takeSnapshot {
            filterMapState.setSelected(1)
            assertEquals(1, filterMapState.selected)
        }
        assertEquals(null, filterMapState.selected)
    }

    @Test
    public fun testSelectOption() {
        var option: Int? = null
        filterMapState.onSelectionChange = { option = it }
        filterMapState.selectOption(1)
        assertEquals(1, option)
    }
}
