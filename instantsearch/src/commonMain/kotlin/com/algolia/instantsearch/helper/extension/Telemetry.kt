@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.map.FilterMapConnector
import com.algolia.instantsearch.helper.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.helper.filter.range.FilterRangeConnector
import com.algolia.instantsearch.helper.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.toByteArray
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.search.Facet
import io.ktor.util.*

internal fun telemetrySchema(): String? {
    @OptIn(InternalAPI::class) // TODO: replace `encodeBase64` with internal implementation
    return GlobalTelemetry.schema()?.toByteArray()?.encodeBase64()?.let { "Telemetry($it)" }
}

/** Telemetry: trace hits searcher */
internal fun HitsSearcher.traceHitsSearcher() {
    val params = buildSet {
        if (!isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace hits searcher */ // TODO: to be merged with traceSearcher for HitsSearcher
internal fun SearcherSingleIndex.traceHitsSearcher() {
    val params = buildSet {
        if (!isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace facets searcher */
internal fun FacetsSearcher.traceFacetsSearcher() {
    val params = buildSet {
        if (facetQuery != null) add(ComponentParam.FacetsQuery)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace facets searcher */ // TODO: to be merged with traceFacetsSearcher for FacetsSearcher
internal fun SearcherForFacets.traceFacetsSearcher() {
    val params = buildSet {
        if (facetQuery != null) add(ComponentParam.FacetsQuery)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace multi-searcher */
internal fun DefaultMultiSearcher.traceMultiSearcher() {
    val params = buildSet {
        if (strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.MultiSearcher, params)
}

/** Telemetry: trace multi-searcher */ // TODO: to be merged with traceMultiSearcher for DefaultMultiSearcher
internal fun SearcherMultipleIndex.traceMultiSearcher() {
    val params = buildSet {
        if (strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.MultiSearcher, params)
}

/** Telemetry: trace filter state */
internal fun traceFilterState() {
    GlobalTelemetry.trace(ComponentType.FilterState)
}

/** Telemetry: trace loading connector */
internal fun traceLoadingConnector() {
    GlobalTelemetry.traceConnector(ComponentType.Loading)
}

/** Telemetry: trace answers searcher */
internal fun SearcherAnswers.traceAnswersSearcher() {
    val params = if (requestOptions != null) setOf(ComponentParam.RequestOptions) else emptySet()
    GlobalTelemetry.trace(ComponentType.AnswersSearcher, params)
}

/** Telemetry: trace dynamic facets */
internal fun traceDynamicFacet(
    orderedFacets: List<AttributedFacets>,
    selections: SelectionsPerAttribute,
    selectionModeForAttribute: Map<Attribute, SelectionMode>
) {
    val params = buildSet {
        if (orderedFacets != emptyList<AttributedFacets>()) add(ComponentParam.OrderedFacets)
        if (selections != emptyMap<Attribute, Set<String>>()) add(ComponentParam.Selections)
        if (selectionModeForAttribute != emptyMap<Attribute, SelectionMode>()) add(ComponentParam.SelectionModeForAttribute)
    }
    GlobalTelemetry.trace(ComponentType.DynamicFacets, params)
}

/** Telemetry: trace dynamic facets connector */
internal fun traceDynamicFacetConnector(filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor>) {
    val params =
        if (filterGroupForAttribute != emptyMap<Attribute, FilterGroupDescriptor>()) setOf(ComponentParam.FilterGroupForAttribute) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.DynamicFacets, params)
}

/** Telemetry: trace hierarchical menu */
internal fun traceHierarchicalFacets() {
    GlobalTelemetry.trace(ComponentType.HierarchicalFacets)
}

/** Telemetry: trace hierarchical menu connector */
internal fun traceHierarchicalFacetsConnector() {
    GlobalTelemetry.traceConnector(ComponentType.HierarchicalFacets)
}

/** Telemetry: trace facets list */
internal fun traceFacetList(items: List<Facet>, selectionMode: SelectionMode, persistentSelection: Boolean) {
    val params = buildSet {
        if (items != emptyList<Facet>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
        if (persistentSelection) add(ComponentParam.PersistentSelection)
    }
    GlobalTelemetry.trace(ComponentType.FacetList, params)
}

/** Telemetry: trace facets list connector */
internal fun traceFacetListConnector() {
    GlobalTelemetry.traceConnector(ComponentType.FacetList)
}

/** Telemetry: trace filter clear */
internal fun traceFilterClear() {
    GlobalTelemetry.trace(ComponentType.FilterClear)
}

/** Telemetry: trace filter clear connector */
internal fun FilterClearConnector.traceFilterClearConnector() {
    val params = buildSet {
        if (groupIDs != emptyList<FilterGroupID>()) add(ComponentParam.GroupIDs)
        if (mode != ClearMode.Specified) add(ComponentParam.ClearMode)
    }
    GlobalTelemetry.traceConnector(ComponentType.FilterClear, params)
}

/** Telemetry: trace facet filter list */
internal fun FilterListViewModel.Facet.traceFacetFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Facet>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    GlobalTelemetry.trace(ComponentType.FacetFilterList, params)
}

/** Telemetry: trace facet filter list connector */
internal fun FilterListConnector.Facet.traceFacetFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.Or) add(ComponentParam.Operator)
    }
    GlobalTelemetry.traceConnector(ComponentType.FacetFilterList, params)
}

/** Telemetry: trace numeric filter list */
internal fun FilterListViewModel.Numeric.traceNumericFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Numeric>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Single) add(ComponentParam.SelectionMode)
    }
    GlobalTelemetry.trace(ComponentType.NumericFilterList, params)
}

/** Telemetry: trace facet filter list connector */
internal fun FilterListConnector.Numeric.traceNumericFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    GlobalTelemetry.traceConnector(ComponentType.NumericFilterList, params)
}

/** Telemetry: tag numeric filter list */
internal fun FilterListViewModel.Tag.traceTagFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Tag>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    GlobalTelemetry.trace(ComponentType.TagFilterList, params)
}

/** Telemetry: tag numeric filter list connector */
internal fun FilterListConnector.Tag.traceTagFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    GlobalTelemetry.traceConnector(ComponentType.TagFilterList, params)
}

/** Telemetry: trace filter list (all) */
internal fun FilterListViewModel.All.traceFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    GlobalTelemetry.trace(ComponentType.FilterList, params)
}

/** Telemetry: trace filter list connector (all) */
internal fun FilterListConnector.All.traceFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    GlobalTelemetry.traceConnector(ComponentType.FilterList, params)
}

/** Telemetry: trace filter toggle connector */
internal fun FilterToggleConnector.traceFilterToggleConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.FilterToggle, params)
}

/** Telemetry: trace number filter connector */
internal fun FilterComparisonConnector<*>.traceNumberFilterConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.NumberFilter, params)
}

/** Telemetry: trace number range filter connector */
internal fun FilterRangeConnector<*>.traceNumberRangeFilterConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.NumberRangeFilter, params)
}

/** Telemetry: trace current filters */
internal fun FilterCurrentConnector.traceCurrentFilters() {
    val params = if (groupIDs != emptyList<FilterGroupID>()) setOf(ComponentParam.GroupIDs) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.CurrentFilters, params)
}

/** Telemetry: trace stats */
internal fun traceStats() {
    GlobalTelemetry.trace(ComponentType.Stats)
}

/** Telemetry: trace stats */
internal fun traceStatsConnector() {
    GlobalTelemetry.traceConnector(ComponentType.Stats)
}

/** Telemetry: trace query rule custom data */
internal fun traceQueryRuleCustomData(hasItem: Boolean) {
    val params = if (hasItem) setOf(ComponentParam.Item) else emptySet()
    GlobalTelemetry.trace(ComponentType.QueryRuleCustomData, params)
}

/** Telemetry: trace query rule custom data connector */
internal fun traceQueryRuleCustomDataConnector() {
    GlobalTelemetry.traceConnector(ComponentType.QueryRuleCustomData)
}

/** Telemetry: trace relevant sort connector */
internal fun traceRelevantSortConnector() {
    GlobalTelemetry.traceConnector(ComponentType.RelevantSort)
}

/** Telemetry: trace sort by connector */
internal fun traceSortByConnector() {
    GlobalTelemetry.traceConnector(ComponentType.SortBy)
}

/** Telemetry: trace filter map connector */
internal fun FilterMapConnector.traceFilterMapConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.FilterMap, params)
}

/** Telemetry: trace filter map connector */
internal fun SearchBoxConnector<*>.traceSearchBoxConnector() {
    val params = if (searchMode != SearchMode.AsYouType) setOf(ComponentParam.SearchMode) else emptySet()
    GlobalTelemetry.traceConnector(ComponentType.SearchBox, params)
}

/** Telemetry: trace related items */
internal fun traceRelatedItems() {
    GlobalTelemetry.trace(ComponentType.RelatedItems)
}
