@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry

internal fun Telemetry.traceSearcher(searcher: HitsSearcher) {
    val params = buildList {
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
    }
    GlobalTelemetry.traceConnector(ComponentType.HitsSearcher, params)
}

// TODO: to be merged with traceSearcher for HitsSearcher
internal fun Telemetry.traceSearcher(searcher: SearcherSingleIndex) {
    val params = buildList {
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
        if (!searcher.isDisjunctiveFacetingEnabled) add(ComponentParam.IsDisjunctiveFacetingEnabled)
    }
    GlobalTelemetry.traceConnector(ComponentType.HitsSearcher, params)
}

internal fun Telemetry.traceFacetsSearcher(searcher: FacetsSearcher) {
    val params = buildList {
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
    }
    GlobalTelemetry.traceConnector(ComponentType.FacetSearcher, params)
}

// TODO: to be merged with traceFacetsSearcher for FacetsSearcher
internal fun Telemetry.traceFacetsSearcher(searcher: SearcherForFacets) {
    val params = buildList {
        if (searcher.requestOptions != null) add(ComponentParam.RequestOptions)
        if (searcher.facetQuery != null) add(ComponentParam.FacetsQuery)
    }
    GlobalTelemetry.traceConnector(ComponentType.FacetSearcher, params)
}

