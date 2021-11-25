@file:OptIn(Internal::class, ExperimentalInstantSearch::class, ExperimentalStdlibApi::class)

package com.algolia.instantsearch.helper.extension

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.telemetry.ComponentParam.IsDisjunctiveFacetingEnabled
import com.algolia.instantsearch.telemetry.ComponentParam.RequestOptions
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry

internal fun Telemetry.traceSearcher(searcher: HitsSearcher) {
    val params = buildList {
        if (searcher.requestOptions != null) add(RequestOptions)
        if (!searcher.isDisjunctiveFacetingEnabled) add(IsDisjunctiveFacetingEnabled)
    }
    GlobalTelemetry.traceConnector(ComponentType.HitsSearcher, params)
}

internal fun Telemetry.traceSearcher(searcher: SearcherSingleIndex) {
    // TODO: to be merged with traceSearcher for HitsSearcher
    val params = buildList {
        if (searcher.requestOptions != null) add(RequestOptions)
        if (!searcher.isDisjunctiveFacetingEnabled) add(IsDisjunctiveFacetingEnabled)
    }
    GlobalTelemetry.traceConnector(ComponentType.HitsSearcher, params)
}
