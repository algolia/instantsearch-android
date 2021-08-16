package com.algolia.instantsearch.compose.searchbox

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class SearchBoxStateTest {

    @Test
    public fun test() {
        var query: String? = null
        val init = ""
        val searchBoxState = SearchBoxState(init)
        searchBoxState.onQueryChanged = { query = it }
        Snapshot.takeSnapshot {
            val text = "phone"
            searchBoxState.setText(text)
            assertEquals(text, searchBoxState.query)
            assertEquals(text, query)
        }
        assertEquals(init, searchBoxState.query)
    }
}
