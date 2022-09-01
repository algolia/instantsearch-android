package com.algolia.instantsearch.compose.telemetry;

import com.algolia.instantsearch.compose.customdata.QueryRuleCustomDataState
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.compose.filter.map.FilterMapState
import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import com.algolia.instantsearch.compose.hierarchical.HierarchicalState
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.loading.LoadingState
import com.algolia.instantsearch.compose.number.NumberState
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortPriorityState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.telemetry.Component
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.ComponentType.*
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.filter.Filter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

public class TestTelemetry {

    private val scope = TestScope()

    init {
        val telemetry = Telemetry(scope)
        Telemetry.set(telemetry)
    }

    @Test
    public fun testSearchBoxState(): Unit = scope.runTest {
        // initiated at top level
        SearchBoxState()
        Telemetry.shared.validateDeclarativeAndGet(SearchBox)
    }

    @Test
    public fun testQueryRuleCustomDataState(): Unit = scope.runTest {
        QueryRuleCustomDataState<Any>()
        Telemetry.shared.validateDeclarativeAndGet(QueryRuleCustomData)
    }

    @Test
    public fun testFilterClear(): Unit = scope.runTest {
        FilterClear()
        Telemetry.shared.validateDeclarativeAndGet(FilterClear)
    }

    @Test
    public fun testFilterCurrentState(): Unit = scope.runTest {
        FilterCurrentState()
        Telemetry.shared.validateDeclarativeAndGet(CurrentFilters)
    }

    @Test
    public fun testFacetListState(): Unit = scope.runTest {
        FacetListState()
        Telemetry.shared.validateDeclarativeAndGet(FacetList)
    }

    @Test
    public fun testFilterListState(): Unit = scope.runTest {
        FilterListState<Filter>()
        Telemetry.shared.validateDeclarativeAndGet(FilterList)
    }

    @Test
    public fun testFilterMapState(): Unit = scope.runTest {
        FilterMapState()
        Telemetry.shared.validateDeclarativeAndGet(FilterMap)
    }

    @Test
    public fun testFilterToggleState(): Unit = scope.runTest {
        FilterToggleState()
        Telemetry.shared.validateDeclarativeAndGet(FilterToggle)
    }

    @Test
    public fun testHierarchicalState(): Unit = scope.runTest {
        HierarchicalState()
        Telemetry.shared.validateDeclarativeAndGet(HierarchicalFacets)
    }

    @Test
    public fun testHitsState(): Unit = scope.runTest {
        HitsState<Any>()
        Telemetry.shared.validateDeclarativeAndGet(Hits)
    }

    @Test
    public fun testStatsState(): Unit = scope.runTest {
        StatsTextState()
        Telemetry.shared.validateDeclarativeAndGet(Stats)
    }

    @Test
    public fun testLoadingState(): Unit = scope.runTest {
        LoadingState()
        Telemetry.shared.validateDeclarativeAndGet(Loading)
    }

    @Test
    public fun testNumberRangeState(): Unit = scope.runTest {
        NumberRangeState<Int>()
        Telemetry.shared.validateDeclarativeAndGet(NumberRangeFilter)
    }

    @Test
    public fun testRelevantSortState(): Unit = scope.runTest {
        RelevantSortPriorityState()
        Telemetry.shared.validateDeclarativeAndGet(RelevantSort)
    }

    @Test
    public fun testNumberState(): Unit = scope.runTest {
        NumberState<Int>()
        Telemetry.shared.validateDeclarativeAndGet(NumberFilter)
    }

    @Test
    public fun testSortByState(): Unit = scope.runTest {
        SortByState()
        Telemetry.shared.validateDeclarativeAndGet(SortBy)
    }

    private suspend fun Telemetry.validateDeclarativeAndGet(type: ComponentType): Component {
        delay(100)
        val components = schema()?.components?.filter { it.type == type } ?: error("component of type $type not found")
        assertEquals(1, components.size, "should be only one component of type $type")
        val component = components.first()
        assertEquals(true, component.isDeclarative, "the component is not a from declarative state")
        return component
    }
}
