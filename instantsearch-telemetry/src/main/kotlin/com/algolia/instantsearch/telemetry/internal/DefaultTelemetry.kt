package com.algolia.instantsearch.telemetry.internal

import com.algolia.instantsearch.telemetry.*

/**
 * Default [Telemetry] implementation.
 */
internal class DefaultTelemetry : Telemetry {

    private val telemetryComponents = mutableMapOf<ComponentType, List<ComponentParam>>()

    override fun trace(componentType: ComponentType, componentParams: List<ComponentParam>) {
        telemetryComponents[componentType] = componentParams
    }

    override fun schema(): Schema {
        val componentsList = telemetryComponents.map { (componentType, componentParams) ->
            Component.newBuilder().setType(componentType).addAllParameters(componentParams).build()
        }
        return Schema.newBuilder().addAllComponents(componentsList).build()
    }
}
