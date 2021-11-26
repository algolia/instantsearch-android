package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.telemetry.internal.DefaultTelemetry

/**
 * Controller to handle components telemetry operations.
 */
@Internal
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
        componentParams: Set<ComponentParam> = emptySet()
    )

    /**
     * Track a component by its [ComponentType] and [ComponentParam].
     */
    public fun traceConnector(
        componentType: ComponentType,
        componentParams: Set<ComponentParam> = emptySet()
    )
}

/**
 * Creates an instance of [Telemetry].
 */
@Internal
public fun Telemetry(): Telemetry = DefaultTelemetry()