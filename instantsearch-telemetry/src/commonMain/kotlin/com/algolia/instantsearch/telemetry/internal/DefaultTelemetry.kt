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

    override fun traceViewModel(componentType: ComponentType, vararg componentParams: ComponentParam) {
        if (telemetryComponents[componentType]?.isConnector == true) return
        telemetryComponents[componentType] = DataContainer(componentParams.toList(), false)
    }

    override fun traceConnector(componentType: ComponentType, vararg componentParams: ComponentParam) {
        telemetryComponents[componentType] = DataContainer(componentParams.toList(), true)
    }

    override fun schema(): Schema {
        val componentsList = telemetryComponents.map { (type, data) ->
            Component(type, data.params, data.isConnector)
        }
        return Schema(componentsList)
    }

    private data class DataContainer(val params: List<ComponentParam>, val isConnector: Boolean)
}
