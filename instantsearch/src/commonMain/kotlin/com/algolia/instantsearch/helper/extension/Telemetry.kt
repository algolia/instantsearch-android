@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.DefaultMultiSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy

internal fun Telemetry.traceSearcher(searcher: HitsSearcher) {
    val params = buildList {
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.HitsSearcher, params)
}

// TODO: to be merged with traceSearcher for HitsSearcher
internal fun Telemetry.traceSearcher(searcher: SearcherSingleIndex) {
    val params = buildList {
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.HitsSearcher, params)
}

internal fun Telemetry.traceFacetsSearcher(searcher: FacetsSearcher) {
    val params = buildList {
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.FacetSearcher, params)
}

// TODO: to be merged with traceFacetsSearcher for FacetsSearcher
internal fun Telemetry.traceFacetsSearcher(searcher: SearcherForFacets) {
    val params = buildList {
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.FacetSearcher, params)
}

internal fun Telemetry.traceMultiSearcher(searcher: DefaultMultiSearcher) {
    val params = buildList {
        if (searcher.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.MultiSearcher, params)
}

// TODO: to be merged with traceMultiSearcher for DefaultMultiSearcher
internal fun Telemetry.traceMultiSearcher(searcher: SearcherMultipleIndex) {
    val params = buildList {
        if (searcher.strategy != MultipleQueriesStrategy.None) add(ComponentParam.Strategy)
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
    }
    GlobalTelemetry.traceWidget(ComponentType.MultiSearcher, params)
}
