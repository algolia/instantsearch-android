@file:OptIn(InternalInstantSearch::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)
@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.InternalInstantSearch
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.encode.gzip
import com.algolia.instantsearch.filter.clear.ClearMode
import com.algolia.instantsearch.filter.clear.FilterClearConnector
import com.algolia.instantsearch.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.FilterListViewModel
import com.algolia.instantsearch.filter.map.FilterMapConnector
import com.algolia.instantsearch.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.filter.range.FilterRangeConnector
import com.algolia.instantsearch.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searcher.SearcherAnswers
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Schema
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.instantsearch.telemetry.toByteArray
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.search.Facet
import io.ktor.util.encodeBase64

/** Get telemetry schema header **/
internal fun telemetrySchema(): String? {
    return Telemetry.shared.schema()?.compress()?.let { "ISTelemetry($it)" }
}

/** Compress [Schema] structure (gzip + base64) */
@OptIn(io.ktor.util.InternalAPI::class)
private fun Schema.compress(): String {
    return toByteArray().gzip().encodeBase64() // TODO: replace ktor's encodeBase64
}

/** Telemetry: trace hits searcher */
internal fun HitsSearcher.traceHitsSearcher() {
    val params = buildSet {
        if (!isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    Telemetry.shared.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace facets searcher */
internal fun FacetsSearcher.traceFacetsSearcher() {
    val params = buildSet {
        if (facetQuery != null) add(ComponentParam.FacetsQuery)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    Telemetry.shared.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace multi-searcher */
internal fun DefaultMultiSearcher.traceMultiSearcher() {
    val params = buildSet {
        if (strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (requestOptions != null) add(ComponentParam.RequestOptions)
    }
    Telemetry.shared.trace(ComponentType.MultiSearcher, params)
}

/** Telemetry: trace filter state */
internal fun traceFilterState() {
    Telemetry.shared.trace(ComponentType.FilterState)
}

/** Telemetry: trace loading connector */
internal fun traceLoadingConnector() {
    Telemetry.shared.traceConnector(ComponentType.Loading)
}

/** Telemetry: trace answers searcher */
internal fun SearcherAnswers.traceAnswersSearcher() {
    val params = if (requestOptions != null) setOf(ComponentParam.RequestOptions) else emptySet()
    Telemetry.shared.trace(ComponentType.AnswersSearcher, params)
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
    Telemetry.shared.trace(ComponentType.DynamicFacets, params)
}

/** Telemetry: trace dynamic facets connector */
internal fun traceDynamicFacetConnector(filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor>) {
    val params =
        if (filterGroupForAttribute != emptyMap<Attribute, FilterGroupDescriptor>()) setOf(ComponentParam.FilterGroupForAttribute) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.DynamicFacets, params)
}

/** Telemetry: trace hierarchical menu */
internal fun traceHierarchicalFacets() {
    Telemetry.shared.trace(ComponentType.HierarchicalFacets)
}

/** Telemetry: trace hierarchical menu connector */
internal fun traceHierarchicalFacetsConnector() {
    Telemetry.shared.traceConnector(ComponentType.HierarchicalFacets)
}

/** Telemetry: trace facets list */
internal fun traceFacetList(items: List<Facet>, selectionMode: SelectionMode, persistentSelection: Boolean) {
    val params = buildSet {
        if (items != emptyList<Facet>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
        if (persistentSelection) add(ComponentParam.PersistentSelection)
    }
    Telemetry.shared.trace(ComponentType.FacetList, params)
}

/** Telemetry: trace facets list connector */
internal fun traceFacetListConnector() {
    Telemetry.shared.traceConnector(ComponentType.FacetList)
}

/** Telemetry: trace filter clear */
internal fun traceFilterClear() {
    Telemetry.shared.trace(ComponentType.FilterClear)
}

/** Telemetry: trace filter clear connector */
internal fun FilterClearConnector.traceFilterClearConnector() {
    val params = buildSet {
        if (groupIDs != emptyList<FilterGroupID>()) add(ComponentParam.GroupIDs)
        if (mode != ClearMode.Specified) add(ComponentParam.ClearMode)
    }
    Telemetry.shared.traceConnector(ComponentType.FilterClear, params)
}

/** Telemetry: trace facet filter list */
internal fun FilterListViewModel.Facet.traceFacetFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Facet>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    Telemetry.shared.trace(ComponentType.FacetFilterList, params)
}

/** Telemetry: trace facet filter list connector */
internal fun FilterListConnector.Facet.traceFacetFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.Or) add(ComponentParam.Operator)
    }
    Telemetry.shared.traceConnector(ComponentType.FacetFilterList, params)
}

/** Telemetry: trace numeric filter list */
internal fun FilterListViewModel.Numeric.traceNumericFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Numeric>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Single) add(ComponentParam.SelectionMode)
    }
    Telemetry.shared.trace(ComponentType.NumericFilterList, params)
}

/** Telemetry: trace facet filter list connector */
internal fun FilterListConnector.Numeric.traceNumericFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    Telemetry.shared.traceConnector(ComponentType.NumericFilterList, params)
}

/** Telemetry: tag numeric filter list */
internal fun FilterListViewModel.Tag.traceTagFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter.Tag>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    Telemetry.shared.trace(ComponentType.TagFilterList, params)
}

/** Telemetry: tag numeric filter list connector */
internal fun FilterListConnector.Tag.traceTagFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    Telemetry.shared.traceConnector(ComponentType.TagFilterList, params)
}

/** Telemetry: trace filter list (all) */
internal fun FilterListViewModel.All.traceFilterList() {
    val params = buildSet {
        if (items.value != emptyList<Filter>()) add(ComponentParam.Items)
        if (selectionMode != SelectionMode.Multiple) add(ComponentParam.SelectionMode)
    }
    Telemetry.shared.trace(ComponentType.FilterList, params)
}

/** Telemetry: trace filter list connector (all) */
internal fun FilterListConnector.All.traceFilterListConnector() {
    val params = buildSet {
        if (groupID.operator != FilterOperator.And) add(ComponentParam.Operator)
    }
    Telemetry.shared.traceConnector(ComponentType.FilterList, params)
}

/** Telemetry: trace filter toggle connector */
internal fun FilterToggleConnector.traceFilterToggleConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.FilterToggle, params)
}

/** Telemetry: trace number filter connector */
internal fun FilterComparisonConnector<*>.traceNumberFilterConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.NumberFilter, params)
}

/** Telemetry: trace number range filter connector */
internal fun FilterRangeConnector<*>.traceNumberRangeFilterConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.NumberRangeFilter, params)
}

/** Telemetry: trace current filters */
internal fun FilterCurrentConnector.traceCurrentFilters() {
    val params = if (groupIDs != emptyList<FilterGroupID>()) setOf(ComponentParam.GroupIDs) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.CurrentFilters, params)
}

/** Telemetry: trace stats */
internal fun traceStats() {
    Telemetry.shared.trace(ComponentType.Stats)
}

/** Telemetry: trace stats */
internal fun traceStatsConnector() {
    Telemetry.shared.traceConnector(ComponentType.Stats)
}

/** Telemetry: trace query rule custom data */
internal fun traceQueryRuleCustomData(hasItem: Boolean) {
    val params = if (hasItem) setOf(ComponentParam.Item) else emptySet()
    Telemetry.shared.trace(ComponentType.QueryRuleCustomData, params)
}

/** Telemetry: trace query rule custom data connector */
internal fun traceQueryRuleCustomDataConnector() {
    Telemetry.shared.traceConnector(ComponentType.QueryRuleCustomData)
}

/** Telemetry: trace relevant sort connector */
internal fun traceRelevantSortConnector() {
    Telemetry.shared.traceConnector(ComponentType.RelevantSort)
}

/** Telemetry: trace sort by connector */
internal fun traceSortByConnector() {
    Telemetry.shared.traceConnector(ComponentType.SortBy)
}

/** Telemetry: trace filter map connector */
internal fun FilterMapConnector.traceFilterMapConnector() {
    val params = if (groupID.operator != FilterOperator.And) setOf(ComponentParam.Operator) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.FilterMap, params)
}

/** Telemetry: trace filter map connector */
internal fun SearchBoxConnector<*>.traceSearchBoxConnector() {
    val params = if (searchMode != SearchMode.AsYouType) setOf(ComponentParam.SearchMode) else emptySet()
    Telemetry.shared.traceConnector(ComponentType.SearchBox, params)
}

/** Telemetry: trace related items */
internal fun traceRelatedItems() {
    Telemetry.shared.trace(ComponentType.RelatedItems)
}
