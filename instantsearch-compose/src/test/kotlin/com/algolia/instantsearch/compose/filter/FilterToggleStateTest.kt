package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import kotlin.test.Test
import kotlin.test.assertEquals

public class FilterToggleStateTest {

    private val initText = "unselected"
    private val initIsSelected = false
    private val filterToggleState = FilterToggleState(initText, initIsSelected)

    @Test
    public fun testText() {
        assertEquals(initText, filterToggleState.text)
        Snapshot.takeSnapshot {
            val item = "selected"
            filterToggleState.setItem(item)
            assertEquals(item, filterToggleState.text)
        }
        assertEquals(initText, filterToggleState.text)
    }

    @Test
    public fun testIsSelected() {
        assertEquals(initIsSelected, filterToggleState.isSelected)
        Snapshot.takeSnapshot {
            val isSelected = true
            filterToggleState.setIsSelected(isSelected)
            assertEquals(isSelected, filterToggleState.isSelected)
        }
        assertEquals(initIsSelected, filterToggleState.isSelected)
    }

    @Test
    public fun testChangeSelection() {
        var changed = false
        filterToggleState.onSelectionChanged = { changed = it }
        filterToggleState.changeSelection(true)
        assertEquals(true, changed)
    }
}
