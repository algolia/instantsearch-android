package com.algolia.instantsearch.compose.internal

import com.algolia.instantsearch.compose.customdata.QueryRuleCustomDataState
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.compose.filter.map.FilterMapState
import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import com.algolia.instantsearch.compose.hierarchical.HierarchicalState
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.item.StatsState
import com.algolia.instantsearch.compose.loading.LoadingState
import com.algolia.instantsearch.compose.number.NumberState
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.telemetry.ComponentType.*
import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Trace [SearchBoxState].
 */
internal fun SearchBoxState.trace() {
    Telemetry.shared.traceDeclarative(SearchBox)
}

/**
 * Trace [QueryRuleCustomDataState].
 */
internal fun QueryRuleCustomDataState<*>.trace() {
    Telemetry.shared.traceDeclarative(QueryRuleCustomData)
}

/**
 * Trace [FilterClear].
 */
internal fun FilterClear.trace() {
    Telemetry.shared.traceDeclarative(FilterClear)
}

/**
 * Trace [FilterCurrentState].
 */
internal fun FilterCurrentState.trace() {
    Telemetry.shared.traceDeclarative(CurrentFilters)
}

/**
 * Trace [FacetListState].
 */
internal fun FacetListState.trace() {
    Telemetry.shared.traceDeclarative(FacetList)
}

/**
 * Trace [FilterListState].
 */
internal fun FilterListState<*>.trace() {
    Telemetry.shared.traceDeclarative(FilterList)
}

/**
 * Trace [FilterMapState].
 */
internal fun FilterMapState.trace() {
    Telemetry.shared.traceDeclarative(FilterMap)
}

/**
 * Trace [FilterToggleState].
 */
internal fun FilterToggleState.trace() {
    Telemetry.shared.traceDeclarative(FilterToggle)
}

/**
 * Trace [HierarchicalState].
 */
internal fun HierarchicalState.trace() {
    Telemetry.shared.traceDeclarative(HierarchicalFacets)
}

/**
 * Trace [HitsState]
 */
internal fun HitsState<*>.trace() {
    Telemetry.shared.traceDeclarative(Hits)
}

/**
 * Trace [StatsState].
 */
internal fun StatsState<*>.trace() {
    Telemetry.shared.traceDeclarative(Stats)
}

/**
 * Trace [LoadingState].
 */
internal fun LoadingState.trace() {
    Telemetry.shared.traceDeclarative(Loading)
}

/**
 * Trace [NumberRangeState].
 */
internal fun NumberRangeState<*>.trace() {
    Telemetry.shared.traceDeclarative(NumberRangeFilter)
}

/**
 * Trace [RelevantSortState].
 */
internal fun RelevantSortState<*>.trace() {
    Telemetry.shared.traceDeclarative(RelevantSort)
}

/**
 * Trace [NumberState].
 */
internal fun NumberState<*>.trace() {
    Telemetry.shared.traceDeclarative(NumberFilter)
}

/**
 * Trace [SortByState].
 */
internal fun SortByState.trace() {
    Telemetry.shared.traceDeclarative(SortBy)
}
