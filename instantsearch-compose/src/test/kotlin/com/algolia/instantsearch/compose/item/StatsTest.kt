package com.algolia.instantsearch.compose.item

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class StatsTest {

    @Test
    public fun testStats() {
        val stats = StatsTextState("0")
        Snapshot.takeSnapshot {
            stats.setText("42")
            assertEquals("42", stats.stats)
        }
        assertEquals("0", stats.stats)
    }
}
