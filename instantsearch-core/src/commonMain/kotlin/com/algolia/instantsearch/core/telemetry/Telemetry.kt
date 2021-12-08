package com.algolia.instantsearch.core.telemetry

import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.telemetry.Opt
import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Get Shared (global) telemetry object.
 */
public val Telemetry.shared: Opt
    get() = GlobalTelemetry

