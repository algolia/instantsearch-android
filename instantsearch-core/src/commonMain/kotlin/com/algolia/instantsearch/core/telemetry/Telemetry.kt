package com.algolia.instantsearch.core.telemetry

import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.telemetry.Opt

/**
 * Get Shared (global) telemetry object.
 */
public val SharedTelemetry: Opt
    get() = GlobalTelemetry
