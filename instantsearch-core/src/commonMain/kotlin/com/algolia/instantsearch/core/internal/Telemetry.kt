@file:OptIn(ExperimentalStdlibApi::class)

package com.algolia.instantsearch.core.internal

import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Telemetry

/** Telemetry: trace loading widget **/
internal fun traceLoading(isLoading: Boolean) {
    val params = if (isLoading) setOf(ComponentParam.IsLoading) else emptySet()
    Telemetry.shared.trace(ComponentType.Loading, params)
}

/** Telemetry: trace filter toggle */
internal fun traceFilterToggle(isSelected: Boolean) {
    val params = if (isSelected) setOf(ComponentParam.IsSelected) else emptySet()
    Telemetry.shared.trace(ComponentType.FilterToggle, params)
}

/** Telemetry: trace number filter */
internal fun traceNumberFilter(
    hasNumber: Boolean,
    hasBounds: Boolean
) {
    val params = buildSet {
        if (hasNumber) add(ComponentParam.Number)
        if (hasBounds) add(ComponentParam.Bounds)
    }
    Telemetry.shared.trace(ComponentType.NumberFilter, params)
}

/** Telemetry: trace number range filter */
internal fun traceNumberRangeFilter(hasRange: Boolean, hasBounds: Boolean) {
    val params = buildSet {
        if (hasRange) add(ComponentParam.Range)
        if (hasBounds) add(ComponentParam.Bounds)
    }
    Telemetry.shared.trace(ComponentType.NumberRangeFilter, params)
}

/** Telemetry: trace current filters */
internal fun traceCurrentFilters(hasItems: Boolean) {
    val params = if (hasItems) setOf(ComponentParam.Items) else emptySet()
    Telemetry.shared.trace(ComponentType.CurrentFilters, params)
}

/** Telemetry: trace relevant sort */
internal fun traceRelevantSort(hasPriority: Boolean) {
    val params = if (hasPriority) setOf(ComponentParam.Priority) else emptySet()
    Telemetry.shared.trace(ComponentType.RelevantSort, params)
}

/** Telemetry: trace sort by */
internal fun traceSortBy() {
    Telemetry.shared.trace(ComponentType.SortBy)
}

/** Telemetry: trace filter map */
internal fun traceFilterMap() {
    Telemetry.shared.trace(ComponentType.FilterMap)
}

/** Telemetry: trace search box */
internal fun traceSearchBox() {
    Telemetry.shared.trace(ComponentType.SearchBox)
}

/** Telemetry: trace hits */
internal fun traceHits() {
    Telemetry.shared.trace(ComponentType.Hits)
}
