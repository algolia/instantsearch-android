@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy

/** Telemetry: trace hits searcher */
internal fun traceHitsSearcher(searcher: HitsSearcher) {
    val params = buildSet {
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace hits searcher */ // TODO: to be merged with traceSearcher for HitsSearcher
internal fun traceHitsSearcher(searcher: SearcherSingleIndex) {
    val params = buildSet {
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.HitsSearcher, params)
}

/** Telemetry: trace facets searcher */
internal fun traceFacetsSearcher(searcher: FacetsSearcher) {
    val params = buildSet {
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace facets searcher */ // TODO: to be merged with traceFacetsSearcher for FacetsSearcher
internal fun traceFacetsSearcher(searcher: SearcherForFacets) {
    val params = buildSet {
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.FacetSearcher, params)
}

/** Telemetry: trace multi-searcher */
internal fun traceMultiSearcher(searcher: DefaultMultiSearcher) {
    val params = buildSet {
        if (searcher.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.trace(ComponentType.MultiSearcher, params)
}

/** Telemetry: trace multi-searcher */ // TODO: to be merged with traceMultiSearcher for DefaultMultiSearcher
internal fun traceMultiSearcher(searcher: SearcherMultipleIndex) {
    val params = buildSet {
        if (searcher.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
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
internal fun traceAnswersSearcher(searcher: SearcherAnswers) {
    val params = if (searcher.requestOptions != null) setOf(ComponentParam.RequestOptions) else emptySet()
    GlobalTelemetry.trace(ComponentType.AnswersSearcher, params)
}
