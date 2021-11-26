package com.algolia.instantsearch.core.internal

import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry

/** Global telemetry controller */
@Internal
public val GlobalTelemetry: Telemetry = Telemetry()

/** Telemetry: trace loading widget **/
internal fun traceLoading(widget: LoadingViewModel) {
    val params = if (widget.isLoading.value) listOf(ComponentParam.IsLoading) else emptyList()
    GlobalTelemetry.trace(ComponentType.Loading, params)
}
