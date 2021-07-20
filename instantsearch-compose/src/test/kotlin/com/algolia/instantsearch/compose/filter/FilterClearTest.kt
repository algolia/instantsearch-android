package com.algolia.instantsearch.compose.filter

import com.algolia.instantsearch.compose.filter.clear.FilterClear
import kotlin.test.Test
import kotlin.test.assertEquals

public class FilterClearTest {

    @Test
    public fun testClear() {
        var action = ""
        val filterClear = FilterClear().apply {
            onClear = { action = "cleared" }
        }
        assertEquals("", action)
        filterClear.clear()
        assertEquals("cleared", action)
    }
}
