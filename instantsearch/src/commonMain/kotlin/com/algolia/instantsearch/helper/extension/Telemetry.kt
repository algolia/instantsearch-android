@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.helper.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.search.model.Attribute
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy

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
