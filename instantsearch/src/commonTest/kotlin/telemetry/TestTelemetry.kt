package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
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
        val viewModel = LoadingViewModel(isLoading = true)
        LoadingConnector(searcher, viewModel)
        val components = GlobalTelemetry.componentsFor(ComponentType.Loading)
        assertEquals(1, components.size)
        val component = components.first()
        assertEquals(setOf(ComponentParam.IsLoading), component.parameters)
        assertEquals(true, component.isConnector)
    }

    @Test
    fun testHitsSearcher() {
        HitsSearcher(client, indexName, isDisjunctiveFacetingEnabled = false)
        val components = GlobalTelemetry.componentsFor(ComponentType.HitsSearcher)
        assertEquals(1, components.size)
        val component = components.first()
        assertEquals(setOf(ComponentParam.IsDisjunctiveFacetingEnabled), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testFacetsSearcher() {
        FacetsSearcher(client, indexName, facetQuery = "a", attribute = Attribute("attr"))
        val components = GlobalTelemetry.componentsFor(ComponentType.FacetSearcher)
        assertEquals(1, components.size)
        val component = components.first()
        assertEquals(setOf(ComponentParam.FacetsQuery), component.parameters)
        assertEquals(false, component.isConnector)

    }

    @Test
    fun testMultiSearcher() {
        MultiSearcher(client, MultipleQueriesStrategy.StopIfEnoughMatches)
        val components = GlobalTelemetry.componentsFor(ComponentType.MultiSearcher)
        assertEquals(1, components.size)
        val component = components.first()
        assertEquals(setOf(ComponentParam.Strategy), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testFilterState() {
        FilterState()
        val components = GlobalTelemetry.componentsFor(ComponentType.FilterState)
        assertEquals(1, components.size)
        val component = components.first()
        assertEquals(emptySet(), component.parameters)
        assertEquals(false, component.isConnector)
    }

    private fun Telemetry.componentsFor(type: ComponentType) = schema().components.filter { it.type == type }
}
