package com.algolia.instantsearch.compose.hierarchical

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.search.model.search.Facet
import kotlin.test.Test
import kotlin.test.assertEquals

public class HierarchicalStateTest {

    @Test
    public fun testHierarchicalItems() {
        val hierarchicalState = HierarchicalState()
        Snapshot.takeSnapshot {
            val elements = listOf(HierarchicalItem(Facet("android", 1), "Android", 1))
            hierarchicalState.setTree(elements)
            assertEquals(elements, hierarchicalState.hierarchicalItems)
        }
        assertEquals(emptyList(), hierarchicalState.hierarchicalItems)
    }
}
