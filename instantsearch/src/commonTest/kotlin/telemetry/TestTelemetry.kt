package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.map.FilterMapConnector
import com.algolia.instantsearch.helper.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.helper.filter.range.FilterRangeConnector
import com.algolia.instantsearch.helper.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.helper.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.helper.relevantsort.RelevantSortConnector
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.stats.StatsConnector
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

    @Test
    fun testNumberRangeFilter() {
        FilterComparisonConnector(
            NumberViewModel(12, Range(10, 42)),
            filterState,
            attribute,
            NumericOperator.Greater,
            FilterGroupID(FilterOperator.Or)
        )
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.NumberFilter)
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
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.NumberRangeFilter)
        assertEquals(setOf(ComponentParam.Operator, ComponentParam.Range, ComponentParam.Bounds), component.parameters)
    }

    @Test
    fun testCurrentFilters() {
        val filter = Filter.Facet(attribute, "attr")
        val filterGroupID = FilterGroupID(attribute, FilterOperator.And)
        FilterCurrentConnector(mapOf((filterGroupID to filter) to filter), filterState, listOf(filterGroupID))
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.CurrentFilters)
        assertEquals(setOf(ComponentParam.GroupIDs, ComponentParam.Items), component.parameters)
    }

    @Test
    fun testStats() {
        StatsConnector(searcherSingleIndex)
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.Stats)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testQueryRuleCustomData() {
        QueryRuleCustomDataConnector(searcherSingleIndex, "init")
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.QueryRuleCustomData)
        assertEquals(setOf(ComponentParam.Item), component.parameters)
    }

    @Test
    fun testRelevantSort() {
        RelevantSortConnector(searcherSingleIndex, RelevantSortViewModel(RelevantSortPriority.HitsCount))
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.RelevantSort)
        assertEquals(setOf(ComponentParam.Priority), component.parameters)
    }

    @Test
    fun testSortBy() {
        SortByConnector(searcherSingleIndex)
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.SortBy)
        assertEquals(emptySet(), component.parameters)
    }

    @Test
    fun testFilterMap() {
        FilterMapConnector(filterState, groupID = FilterGroupID(FilterOperator.Or))
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.FilterMap)
        assertEquals(setOf(ComponentParam.Operator), component.parameters)
    }

    @Test
    fun testSearchBox() {
        SearchBoxConnector(searcherSingleIndex, searchMode = SearchMode.OnSubmit)
        val component = GlobalTelemetry.validateConnectorAndGet(ComponentType.SearchBox)
        assertEquals(setOf(ComponentParam.SearchMode), component.parameters)
    }

    @Test
    fun testRelatedItems() {
        val patternBrand = MatchingPattern(Attribute("attBrand"), 1, SimpleProduct::brand)
        searcherSingleIndex.connectRelatedHitsView(
            mockHitsView(),
            SimpleProduct(ObjectID("id"), "brand"),
            listOf(patternBrand)
        ) {
            listOf(SimpleProduct(ObjectID("objectId"), "brand"))
        }
        val component = GlobalTelemetry.validateAndGet(ComponentType.RelatedItems)
        assertEquals(emptySet(), component.parameters)
    }

    private fun Telemetry.validateAndGet(type: ComponentType): Component {
        val components = schema().components.filter { it.type == type }
        assertEquals(1, components.size, "should be only one component of type $type")
        return components.first()
    }

    private fun Telemetry.validateConnectorAndGet(type: ComponentType): Component {
        val component = validateAndGet(type)
        assertEquals(true, component.isConnector, "the component is not a connector")
        return component
    }
}
