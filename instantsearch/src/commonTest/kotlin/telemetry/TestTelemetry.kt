@file:Suppress("DEPRECATION")

package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.InternalInstantSearch
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.filter.clear.ClearMode
import com.algolia.instantsearch.filter.clear.FilterClearConnector
import com.algolia.instantsearch.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.map.FilterMapConnector
import com.algolia.instantsearch.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.filter.range.FilterRangeConnector
import com.algolia.instantsearch.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.loading.LoadingConnector
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searcher.SearcherAnswers
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.sortby.SortByConnector
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.telemetry.Component
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.search.Facet
import com.algolia.search.transport.RequestOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import mockClient
import relatedItems.SimpleProduct
import relatedItems.mockHitsView

@OptIn(ExperimentalInstantSearch::class, InternalInstantSearch::class)
class TestTelemetry { // instrumented because it uses android's Base64

    val client = mockClient()
    val indexName = IndexName("myIndex")

    val filterState = FilterState()
    val hitsSearcher = HitsSearcher(client, indexName, isDisjunctiveFacetingEnabled = false)
    val attribute = Attribute("attr")

    @Test
    fun testHitsSearcher() {
        // initiated at top level
        val component = Telemetry.shared.validateAndGet(ComponentType.HitsSearcher)
        assertEquals(setOf(ComponentParam.IsDisjunctiveFacetingEnabled), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testFilterState() {
        // initiated at top level
        val component = Telemetry.shared.validateAndGet(ComponentType.FilterState)
        assertEquals(emptySet(), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testLoading() {
        val searcher = HitsSearcher(client, indexName)
        val viewModel = LoadingViewModel(isLoading = true)
        LoadingConnector(searcher, viewModel)
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.Loading)
        assertEquals(setOf(ComponentParam.IsLoading), component.parameters)
    }

    @Test
    fun testFacetsSearcher() {
        FacetsSearcher(client, indexName, facetQuery = "a", attribute = attribute)
        val component = Telemetry.shared.validateAndGet(ComponentType.FacetSearcher)
        assertEquals(setOf(ComponentParam.FacetsQuery), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testMultiSearcher() {
        MultiSearcher(client, MultipleQueriesStrategy.StopIfEnoughMatches)
        val component = Telemetry.shared.validateAndGet(ComponentType.MultiSearcher)
        assertEquals(setOf(ComponentParam.Strategy), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testAnswersSearcher() {
        SearcherAnswers(client.initIndex(indexName), requestOptions = RequestOptions())
        val component = Telemetry.shared.validateAndGet(ComponentType.AnswersSearcher)
        assertEquals(setOf(ComponentParam.RequestOptions), component.parameters)
        assertEquals(false, component.isConnector)
    }

    @Test
    fun testDynamicFilters() {
        DynamicFacetListConnector(
            searcher = hitsSearcher,
            filterState = filterState,
            orderedFacets = listOf(AttributedFacets(attribute, listOf())),
            selections = mapOf(attribute to setOf()),
            selectionModeForAttribute = mapOf(attribute to SelectionMode.Single),
            filterGroupForAttribute = mapOf(attribute to (attribute to FilterOperator.Or))
        )
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.DynamicFacets)
        assertEquals(
            setOf(
                ComponentParam.OrderedFacets,
                ComponentParam.Selections,
                ComponentParam.SelectionModeForAttribute,
                ComponentParam.FilterGroupForAttribute
            ),
            component.parameters
        )
    }

    @Test
    fun testHierarchicalFacets() {
        HierarchicalConnector(hitsSearcher, attribute, filterState, listOf(attribute), "")
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.HierarchicalFacets)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testFacetList() {
        FacetListConnector(
            hitsSearcher,
            filterState,
            attribute,
            SelectionMode.Single,
            listOf(Facet("facet", 1)),
            true
        )
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FacetList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.PersistentSelection),
            component.parameters
        )
    }

    @Test
    fun testFilterClear() {
        FilterClearConnector(filterState, listOf(FilterGroupID(attribute)), ClearMode.Except)
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FilterClear)
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
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FacetFilterList)
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
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.NumericFilterList)
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
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.TagFilterList)
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
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FilterList)
        assertEquals(
            setOf(ComponentParam.Items, ComponentParam.SelectionMode, ComponentParam.Operator),
            component.parameters
        )
    }

    @Test
    fun testFilterToggle() {
        FilterToggleConnector(filterState, Filter.Facet(attribute, "attr"), true, FilterGroupID(FilterOperator.Or))
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FilterToggle)
        assertEquals(setOf(ComponentParam.Operator, ComponentParam.IsSelected), component.parameters)
    }

    @Test
    fun testNumberRangeFilter() {
        FilterComparisonConnector(
            NumberViewModel(12, Range(10, 42)),
            filterState,
            attribute,
            NumericOperator.Greater,
            FilterGroupID(FilterOperator.Or)
        )
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.NumberFilter)
        assertEquals(setOf(ComponentParam.Operator, ComponentParam.Number, ComponentParam.Bounds), component.parameters)
    }

    @Test
    fun testNumberRangeFilterConnector() {
        FilterRangeConnector(
            FilterRangeViewModel(Range(1..10), Range(4..6)),
            filterState,
            attribute,
            FilterGroupID(FilterOperator.Or)
        )
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.NumberRangeFilter)
        assertEquals(setOf(ComponentParam.Operator, ComponentParam.Range, ComponentParam.Bounds), component.parameters)
    }

    @Test
    fun testCurrentFilters() {
        val filter = Filter.Facet(attribute, "attr")
        val filterGroupID = FilterGroupID(attribute, FilterOperator.And)
        FilterCurrentConnector(mapOf((filterGroupID to filter) to filter), filterState, listOf(filterGroupID))
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.CurrentFilters)
        assertEquals(setOf(ComponentParam.GroupIDs, ComponentParam.Items), component.parameters)
    }

    @Test
    fun testStats() {
        StatsConnector(hitsSearcher)
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.Stats)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testQueryRuleCustomData() {
        QueryRuleCustomDataConnector(hitsSearcher, "init")
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.QueryRuleCustomData)
        assertEquals(setOf(ComponentParam.Item), component.parameters)
    }

    @Test
    fun testRelevantSort() {
        RelevantSortConnector(hitsSearcher, RelevantSortViewModel(RelevantSortPriority.HitsCount))
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.RelevantSort)
        assertEquals(setOf(ComponentParam.Priority), component.parameters)
    }

    @Test
    fun testSortBy() {
        SortByConnector(hitsSearcher)
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.SortBy)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testFilterMap() {
        FilterMapConnector(filterState, groupID = FilterGroupID(FilterOperator.Or))
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.FilterMap)
        assertEquals(setOf(ComponentParam.Operator), component.parameters)
    }

    @Test
    fun testSearchBox() {
        SearchBoxConnector(hitsSearcher, searchMode = SearchMode.OnSubmit)
        val component = Telemetry.shared.validateConnectorAndGet(ComponentType.SearchBox)
        assertEquals(setOf(ComponentParam.SearchMode), component.parameters)
    }

    @Test
    fun testRelatedItems() {
        val patternBrand = MatchingPattern(Attribute("attBrand"), 1, SimpleProduct::brand)
        hitsSearcher.connectRelatedHitsView(
            mockHitsView(),
            SimpleProduct(ObjectID("id"), "brand"),
            listOf(patternBrand)
        ) {
            listOf(SimpleProduct(ObjectID("objectId"), "brand"))
        }
        val component = Telemetry.shared.validateAndGet(ComponentType.RelatedItems)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testHits() {
        hitsSearcher.connectHitsView(mockHitsView()) { it.hits }
        val component = Telemetry.shared.validateAndGet(ComponentType.Hits)
        assertEquals(emptySet(), component.parameters)
    }

    private fun Telemetry.validateAndGet(type: ComponentType): Component {
        val components = schema()?.components?.filter { it.type == type } ?: error("component of type $type not found")
        assertEquals(1, components.size, "should be only one component of type $type")
        return components.first()
    }

    private fun Telemetry.validateConnectorAndGet(type: ComponentType): Component {
        val component = validateAndGet(type)
        assertEquals(true, component.isConnector, "the component is not a connector")
        return component
    }
}
