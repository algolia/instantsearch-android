package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import kotlin.test.Test
import kotlin.test.assertEquals

public class FacetListStateTest {

    @Test
    public fun testFacets() {
        val init = emptyList<SelectableItem<FacetHits>>()
        val facetListState = FacetListState(init)
        Snapshot.takeSnapshot {
            val elements = listOf(FacetHits("android", "", 1) to true)
            facetListState.setItems(elements)
            assertEquals(elements, facetListState.items)
        }
        assertEquals(init, facetListState.items)
    }
}
