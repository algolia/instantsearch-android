package com.algolia.instantsearch.compose.number

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortState
import kotlin.test.Test
import kotlin.test.assertEquals

public class RelevantSortStateTest {

    @Test
    public fun testSort() {
        val init = "Relevant"
        val relevantSortState = RelevantSortState(init)
        Snapshot.takeSnapshot {
            val value = "All"
            relevantSortState.updateView(value)
            assertEquals(value, relevantSortState.sort)
        }
        assertEquals(init, relevantSortState.sort)
    }
}
