package com.algolia.instantsearch.telemetry.internal

import com.algolia.instantsearch.telemetry.*

/**
 * Default [Telemetry] implementation.
 */
internal class DefaultTelemetry : Telemetry {

    private val telemetryComponents = mutableMapOf<ComponentType, DataContainer>()

    override fun trace(componentType: ComponentType, componentParams: List<ComponentParam>, isConnector: Boolean) {
        telemetryComponents[componentType] = DataContainer(componentParams, isConnector)
    }

    override fun schema(): Schema {
        val componentsList = telemetryComponents.map { (type, data) ->
            Component(type, data.params, data.isConnector)
        }
        return Schema(componentsList)
    }

    private data class DataContainer(val params: List<ComponentParam>, val isConnector: Boolean)
}
