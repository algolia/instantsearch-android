package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.helper.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.telemetry.Component
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.search.Facet
import com.algolia.search.transport.RequestOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import mockClient

@OptIn(ExperimentalInstantSearch::class, Internal::class)
class TestTelemetry {

    val client = mockClient()
    val indexName = IndexName("myIndex")

    val filterState = FilterState()
    val searcherSingleIndex = SearcherSingleIndex(client.initIndex(indexName), isDisjunctiveFacetingEnabled = false)
    val attribute = Attribute("attr")

    @Test
    fun testHitsSearcher() {
        // initiated at top level
        val component = GlobalTelemetry.validateAndGet(ComponentType.HitsSearcher)
        assertEquals(setOf(ComponentParam.IsDisjunctiveFacetingEnabled), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testFilterState() {
        // initiated at top level
        val component = GlobalTelemetry.validateAndGet(ComponentType.FilterState)
        assertEquals(emptySet(), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testLoading() {
        val searcher = HitsSearcher(client, indexName)
        val viewModel = LoadingViewModel(isLoading = true)
        LoadingConnector(searcher, viewModel)
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.Loading)
        assertEquals(setOf(ComponentParam.IsLoading), component.parameters)
    }

    @Test
    fun testFacetsSearcher() {
        FacetsSearcher(client, indexName, facetQuery = "a", attribute = attribute)
        val component = GlobalTelemetry.validateAndGet(ComponentType.FacetSearcher)
        assertEquals(setOf(ComponentParam.FacetsQuery), component.parameters)
        assertEquals(false, component.isConnector)

    }

    @Test
    fun testMultiSearcher() {
        MultiSearcher(client, MultipleQueriesStrategy.StopIfEnoughMatches)
        val component = GlobalTelemetry.validateAndGet(ComponentType.MultiSearcher)
        assertEquals(setOf(ComponentParam.Strategy), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testAnswersSearcher() {
        SearcherAnswers(client.initIndex(indexName), requestOptions = RequestOptions())
        val component = GlobalTelemetry.validateAndGet(ComponentType.AnswersSearcher)
        assertEquals(setOf(ComponentParam.RequestOptions), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testDynamicFilters() {
        DynamicFacetListConnector(
            searcher = searcherSingleIndex,
            filterState = filterState,
            orderedFacets = listOf(AttributedFacets(attribute, listOf())),
            selections = mapOf(attribute to setOf()),
            selectionModeForAttribute = mapOf(attribute to SelectionMode.Single),
            filterGroupForAttribute = mapOf(attribute to (attribute to FilterOperator.Or))
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.DynamicFacets)
        assertEquals(
            setOf(
                ComponentParam.OrderedFacets,
                ComponentParam.Selections,
                ComponentParam.SelectionModeForAttribute,
                ComponentParam.FilterGroupForAttribute
            ), component.parameters
        )
    }

    @Test
    fun testHierarchicalFacets() {
        HierarchicalConnector(searcherSingleIndex, attribute, filterState, listOf(attribute), "")
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.HierarchicalFacets)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testFacetList() {
        FacetListConnector(
            searcherSingleIndex,
            filterState,
            attribute,
            SelectionMode.Single,
            listOf(Facet("facet", 1)),
            true
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FacetList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.PersistentSelection),
            component.parameters
        )
    }

    @Test
    fun testFilterClear() {
        FilterClearConnector(filterState, listOf(FilterGroupID(attribute)), ClearMode.Except)
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FilterClear)
        assertEquals(setOf(ComponentParam.GroupIDs, ComponentParam.ClearMode), component.parameters)
    }

    @Test
    fun testFacetFilterList() {
        FilterListConnector.Facet(
            listOf(Filter.Facet(attribute, "attr")),
            filterState,
            SelectionMode.Single,
            FilterGroupID(FilterOperator.And)
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FacetFilterList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.Operator),
            component.parameters
        )
    }

    @Test
    fun testNumericFilterList() {
        FilterListConnector.Numeric(
            listOf(Filter.Numeric(attribute, 1..4)),
            filterState,
            SelectionMode.Multiple,
            FilterGroupID(FilterOperator.Or)
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.NumericFilterList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.Operator),
            component.parameters
        )
    }

    @Test
    fun testTagFilterList() {
        FilterListConnector.Tag(
            listOf(Filter.Tag("tag")),
            filterState,
            SelectionMode.Single,
            FilterGroupID(FilterOperator.Or)
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.TagFilterList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.Operator),
            component.parameters
        )
    }

    @Test
    fun testFilterList() {
        FilterListConnector.All(
            listOf(Filter.Tag("tag")),
            filterState,
            SelectionMode.Single,
            FilterGroupID(FilterOperator.Or)
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FilterList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.Operator),
            component.parameters
        )
    }

    @Test
    fun testFilterToggle() {
        FilterToggleConnector(filterState, Filter.Facet(attribute, "attr"), true, FilterGroupID(FilterOperator.Or))
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FilterToggle)
        assertEquals(setOf(ComponentParam.Operator, ComponentParam.IsSelected), component.parameters)
    }

    private fun Telemetry.validateAndGet(type: ComponentType): Component {
        val components = schema().components.filter { it.type == type }
        assertEquals(1, components.size)
        return components.first()
    }

    private fun Telemetry.validateConnectorAndGet(type: ComponentType): Component {
        val component = validateAndGet(type)
        assertEquals(true, component.isConnector)
        return component
    }
}
