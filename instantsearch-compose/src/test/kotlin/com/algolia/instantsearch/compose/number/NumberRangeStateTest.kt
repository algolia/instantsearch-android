package com.algolia.instantsearch.compose.number

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.core.number.range.Range
import kotlin.test.Test
import kotlin.test.assertEquals

public class NumberRangeStateTest {

    private val initRange = Range(1, 3)
    private val initBounds = Range(0, 4)
    private val numberRangeState = NumberRangeState(initRange, initBounds)

    @Test
    public fun testRange() {
        assertEquals(initRange, numberRangeState.range)
        Snapshot.takeSnapshot {
            val range = Range(1, 4)
            numberRangeState.setRange(range)
            assertEquals(range, numberRangeState.range)
        }
        assertEquals(initRange, numberRangeState.range)
    }

    @Test
    public fun testBounds() {
        assertEquals(initBounds, numberRangeState.bounds)
        Snapshot.takeSnapshot {
            val range = Range(0, 3)
            numberRangeState.setBounds(range)
            assertEquals(range, numberRangeState.bounds)
        }
        assertEquals(initBounds, numberRangeState.bounds)
    }
}
