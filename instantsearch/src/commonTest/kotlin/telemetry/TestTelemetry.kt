package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlin.test.Test
import kotlin.test.assertEquals
import mockClient

@OptIn(ExperimentalInstantSearch::class, Internal::class)
class TestTelemetry {

    val client = mockClient()
    val indexName = IndexName("myIndex")

    @Test
    fun testLoading() {
        val searcher = HitsSearcher(client, indexName)
        LoadingConnector(searcher)
        val components = GlobalTelemetry.componentsFor(ComponentType.Loading)
        assertEquals(1, components.size)
        assertEquals(emptyList(), components.first().parameters)
    }

    @Test
    fun testHitsSearcher() {
        HitsSearcher(client, indexName, isDisjunctiveFacetingEnabled = false)
        val components = GlobalTelemetry.componentsFor(ComponentType.HitsSearcher)
        assertEquals(1, components.size)
        assertEquals(listOf(ComponentParam.IsDisjunctiveFacetingEnabled), components.first().parameters)
    }

    @Test
    fun testFacetsSearcher() {
        FacetsSearcher(client, indexName, facetQuery = "a", attribute = Attribute("attr"))
        val components = GlobalTelemetry.componentsFor(ComponentType.FacetSearcher)
        assertEquals(1, components.size)
        assertEquals(listOf(ComponentParam.FacetsQuery), components.first().parameters)
    }

    private fun Telemetry.componentsFor(type: ComponentType) = schema().components.filter { it.type == type }
}
