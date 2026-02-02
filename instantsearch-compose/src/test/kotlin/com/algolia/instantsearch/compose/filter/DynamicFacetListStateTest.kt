package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.snapshots.Snapshot
import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.filter.facet.dynamic.DynamicFacetListState
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

public class DynamicFacetListStateTest {

    private val initOrderedFacets = emptyList<AttributedFacets>()
    private val initSelections = emptyMap<String, Set<String>>()
    private val dynamicFacetListState = DynamicFacetListState(initOrderedFacets, initSelections)

    @Test
    public fun testOrderedFacets() {
        assertEquals(initOrderedFacets, dynamicFacetListState.orderedFacets)
        Snapshot.takeSnapshot {
            val orderedFacets = listOf(AttributedFacets("os", listOf(FacetHits("android", "", 1))))
            dynamicFacetListState.setOrderedFacets(orderedFacets)
            assertEquals(orderedFacets, dynamicFacetListState.orderedFacets)
        }
        assertEquals(initOrderedFacets, dynamicFacetListState.orderedFacets)
    }

    @Test
    public fun testSelections() {
        assertEquals(initSelections, dynamicFacetListState.selections)
        Snapshot.takeSnapshot {
            val selections = mapOf("os" to setOf("android"))
            dynamicFacetListState.setSelections(selections)
            assertEquals(selections, dynamicFacetListState.selections)
        }
        assertEquals(initSelections, dynamicFacetListState.selections)
    }

    @Test
    public fun testToggle() {
        var selectedFacet: FacetHits? = null
        var selectedAttribute: String? = null
        dynamicFacetListState.didSelect = { attribute, facet ->
            selectedFacet = facet
            selectedAttribute = attribute
        }
        val facet = FacetHits("Android", "", 1)
        val attribute = "os"
        dynamicFacetListState.toggle(facet, attribute)

        assertEquals(facet, selectedFacet)
        assertEquals(attribute, selectedAttribute)
    }

    @Test
    public fun testIsSelected() {
        Snapshot.takeSnapshot {
            val attribute = "os"
            val facetAndroid = FacetHits("android", "", 1)
            val facetIOS = FacetHits("ios", "", 1)
            val selections = mapOf(attribute to setOf(facetAndroid.value))
            dynamicFacetListState.setSelections(selections)
            assertTrue(dynamicFacetListState.isSelected(facetAndroid, attribute))
            assertFalse(dynamicFacetListState.isSelected(facetIOS, attribute))
        }
    }
}
