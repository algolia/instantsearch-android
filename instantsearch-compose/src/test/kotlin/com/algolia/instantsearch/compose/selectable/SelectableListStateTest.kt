package com.algolia.instantsearch.compose.selectable

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import kotlin.test.Test
import kotlin.test.assertEquals

public class SelectableListStateTest {

    private val initList = listOf("item" to true)
    private val selectableListState = SelectableListState(initList)

    @Test
    public fun testItems() {
        val itemState = selectableListState.items
        assertEquals(initList, itemState)
        Snapshot.takeSnapshot {
            val items = listOf("newItem" to false)
            selectableListState.setItems(items)
            assertEquals(items, itemState)
        }
        assertEquals(initList, itemState)
    }
}
