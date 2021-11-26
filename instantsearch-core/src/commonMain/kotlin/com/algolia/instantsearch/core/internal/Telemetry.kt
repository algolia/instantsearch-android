package com.algolia.instantsearch.core.internal

import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry

/** Global telemetry controller */
@Internal
public val GlobalTelemetry: Telemetry = Telemetry()

/** Telemetry: trace loading widget **/
internal fun traceLoading(isLoading: Boolean) {
    val params = if (isLoading) setOf(ComponentParam.IsLoading) else emptySet()
    GlobalTelemetry.trace(ComponentType.Loading, params)
}
