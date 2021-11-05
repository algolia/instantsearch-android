package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.telemetry.internal.DefaultTelemetry

/**
 * Controller to handle components telemetry operations.
 */
public interface Telemetry {

    /**
     * Get telemetry [Schema].
     */
    public fun schema(): Schema

    /**
     * Track a component by its [ComponentType] and [ComponentParam].
     */
    public fun trace(
        componentType: ComponentType,
        componentParams: List<ComponentParam> = emptyList(),
        isConnector: Boolean = false
    )
}

/**
 * Creates an instance of [Telemetry].
 */
public fun Telemetry(): Telemetry = DefaultTelemetry()
