package com.algolia.instantsearch.compose.sortby

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class SortByStateTest {

    private val initOptions = mapOf(0 to "android", 1 to "ios")
    private val initSelected = 0
    private val sortByState = SortByState(initOptions, initSelected)

    @Test
    public fun testOptions() {
        assertEquals(initOptions, sortByState.options)
        Snapshot.takeSnapshot {
            val options = mapOf(0 to "android")
            sortByState.setMap(options)
            assertEquals(options, sortByState.options)
        }
        assertEquals(initOptions, sortByState.options)
    }

    @Test
    public fun testSelected() {
        assertEquals(initSelected, sortByState.selected)
        Snapshot.takeSnapshot {
            val selected = 1
            sortByState.setSelected(selected)
            assertEquals(selected, sortByState.selected)
        }
        assertEquals(initSelected, sortByState.selected)
    }
}
