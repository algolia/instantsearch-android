package com.algolia.instantsearch.compose.searchbox

import androidx.compose.runtime.snapshots.Snapshot
import kotlin.test.Test
import kotlin.test.assertEquals

public class SearchQueryTest {

    @Test
    public fun test() {
        var query: String? = null
        val init = ""
        val searchQuery = SearchQuery(init)
        searchQuery.onQueryChanged = { query = it }
        Snapshot.takeSnapshot {
            val text = "phone"
            searchQuery.setText(text, true)
            assertEquals(text, searchQuery.query)
            assertEquals(text, query)
        }
        assertEquals(init, searchQuery.query)
    }
}
