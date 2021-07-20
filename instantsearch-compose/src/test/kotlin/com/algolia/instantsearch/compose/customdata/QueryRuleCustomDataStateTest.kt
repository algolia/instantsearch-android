package com.algolia.instantsearch.compose.customdata

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class QueryRuleCustomDataStateTest {

    @Test
    public fun testItem() {
        val customData = QueryRuleCustomDataState("")
        Snapshot.takeSnapshot {
            customData("android")
            assertEquals("android", customData.item)
        }
        assertEquals("", customData.item)
    }
}
