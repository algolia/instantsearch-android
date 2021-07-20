package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet
import kotlin.test.Test
import kotlin.test.assertEquals

public class FacetListStateTest {

    @Test
    public fun testFacets() {
        val init = emptyList<SelectableItem<Facet>>()
        val facetListState = FacetListState(init)
        Snapshot.takeSnapshot {
            val elements = listOf(Facet("android", 1) to true)
            facetListState.setItems(elements)
            assertEquals(elements, facetListState.facets)
        }
        assertEquals(init, facetListState.facets)
    }
}
