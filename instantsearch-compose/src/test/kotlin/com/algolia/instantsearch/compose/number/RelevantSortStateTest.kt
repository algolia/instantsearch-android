package com.algolia.instantsearch.compose.number

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortPriorityState
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortState
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
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

    @Test
    public fun testPrioritySort() {
        val init = RelevantSortPriority.Relevancy
        val relevantSortState = RelevantSortPriorityState()
        Snapshot.takeSnapshot {
            val value = RelevantSortPriority.HitsCount
            relevantSortState.updateView(value)
            assertEquals(value, relevantSortState.sort)
        }
        assertEquals(init, relevantSortState.sort)
    }
}
