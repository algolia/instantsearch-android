package com.algolia.instantsearch.telemetry.internal

import com.algolia.instantsearch.telemetry.Component
import com.algolia.instantsearch.telemetry.ComponentParam
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.instantsearch.telemetry.Schema
import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Default [Telemetry] implementation.
 */
internal class DefaultTelemetry : Telemetry {

    private val telemetryComponents = mutableMapOf<ComponentType, DataContainer>()

    override fun traceWidget(componentType: ComponentType, componentParams: List<ComponentParam>) {
        if (telemetryComponents[componentType]?.isConnector == true) return
        telemetryComponents[componentType] = DataContainer(componentParams, false)
    }

    override fun traceConnector(componentType: ComponentType, componentParams: List<ComponentParam>) {
        telemetryComponents[componentType] = DataContainer(componentParams, true)
    }

    override fun schema(): Schema {
        val componentsList = telemetryComponents.map { (type, data) ->
            Component(type, data.params, data.isConnector)
        }
        return Schema(componentsList)
    }

    private data class DataContainer(val params: List<ComponentParam>, val isConnector: Boolean)
}
