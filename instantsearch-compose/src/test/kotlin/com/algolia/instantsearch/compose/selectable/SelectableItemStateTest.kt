package com.algolia.instantsearch.compose.selectable

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class SelectableItemStateTest {

    private val initItem = "item" to true
    private val selectableItemState = SelectableItemState(initItem)

    @Test
    public fun testItem() {
        val itemState = selectableItemState.item
        assertEquals(initItem.first, itemState)
        Snapshot.takeSnapshot {
            val item = "newItem"
            selectableItemState.setItem(item)
            assertEquals(item, itemState)
        }
        assertEquals(initItem.first, itemState)
    }

    @Test
    public fun testIsSelected() {
        val isSelectedState = selectableItemState.isSelected
        assertEquals(initItem.second, isSelectedState)
        Snapshot.takeSnapshot {
            val isSelected = false
            selectableItemState.setIsSelected(isSelected)
            assertEquals(isSelected, isSelectedState)
        }
        assertEquals(initItem.second, isSelectedState)
    }

    @Test
    public fun testSelectableItem() {
        val stateSelectableItem = selectableItemState.selectableItem
        assertEquals(initItem, stateSelectableItem)
        Snapshot.takeSnapshot {
            val isSelected = false
            val item = selectableItemState.item
            selectableItemState.setIsSelected(isSelected)
            assertEquals(item to isSelected, stateSelectableItem)
        }
        assertEquals(initItem, stateSelectableItem)
    }
}
