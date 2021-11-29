@file:OptIn(ExperimentalStdlibApi::class)

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

/** Telemetry: trace filter toggle */
internal fun traceFilterToggle(isSelected: Boolean) {
    val params = if (isSelected) setOf(ComponentParam.IsSelected) else emptySet()
    GlobalTelemetry.trace(ComponentType.FilterToggle, params)
}

internal fun traceNumberFilter(
    hasNumber: Boolean,
    hasBounds: Boolean
) {
    val params = buildSet {
        if (hasNumber) add(ComponentParam.Number)
        if (hasBounds) add(ComponentParam.Bounds)
    }
    GlobalTelemetry.trace(ComponentType.NumberFilter, params)
}

internal fun traceNumberRangeFilter(hasRange: Boolean, hasBounds: Boolean) {
    val params = buildSet {
        if (hasRange) add(ComponentParam.Range)
        if (hasBounds) add(ComponentParam.Bounds)
    }
    GlobalTelemetry.trace(ComponentType.NumberRangeFilter, params)
}
