package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.telemetry.internal.DefaultTelemetryController

/**
 * Controller to handle components telemetry operations.
 */
public interface TelemetryController {

    /**
     * Get telemetry [Schema].
     */
    public fun schema(): Schema

    /**
     * Track a [Component].
     */
    public fun track(component: Component)

    /**
     * Track a component by its [ComponentType] and [ComponentParam].
     */
    public fun track(componentType: ComponentType, componentParams: List<ComponentParam>)
}

/**
 * Creates an instance of [TelemetryController].
 */
public fun TelemetryController(): TelemetryController = DefaultTelemetryController()
