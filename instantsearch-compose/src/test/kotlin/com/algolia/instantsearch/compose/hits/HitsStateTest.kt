package com.algolia.instantsearch.compose.hits

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class HitsStateTest {

    @Test
    public fun testHits() {
        val hits = listOf("Apple", "Samsung", "Sony", "LG")
        val hitsState = HitsState(hits)
        Snapshot.takeSnapshot {
            hitsState.setHits(emptyList())
            assertEquals(emptyList(), hitsState.hits)
        }
        assertEquals(hits, hitsState.hits)
    }
}
