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
internal fun traceHitsSearcher(component: HitsSearcher) {
    val params = buildSet {
        if (!component.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace hits searcher */ // TODO: to be merged with traceSearcher for HitsSearcher
internal fun traceHitsSearcher(component: SearcherSingleIndex) {
    val params = buildSet {
        if (!component.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace facets searcher */
internal fun traceFacetsSearcher(component: FacetsSearcher) {
    val params = buildSet {
        if (component.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace facets searcher */ // TODO: to be merged with traceFacetsSearcher for FacetsSearcher
internal fun traceFacetsSearcher(component: SearcherForFacets) {
    val params = buildSet {
        if (component.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace multi-searcher */
internal fun traceMultiSearcher(component: DefaultMultiSearcher) {
    val params = buildSet {
        if (component.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.MultiSearcher, params)
}

/** Telemetry: trace multi-searcher */ // TODO: to be merged with traceMultiSearcher for DefaultMultiSearcher
internal fun traceMultiSearcher(component: SearcherMultipleIndex) {
    val params = buildSet {
        if (component.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (component.requestOptions != null) add(ComponentParam.RequestOptions)
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
internal fun traceAnswersSearcher(component: SearcherAnswers) {
    val params = if (component.requestOptions != null) setOf(ComponentParam.RequestOptions) else emptySet()
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
