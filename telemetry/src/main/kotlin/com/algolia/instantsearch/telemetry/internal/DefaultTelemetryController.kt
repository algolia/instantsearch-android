package com.algolia.instantsearch.telemetry.internal

import com.algolia.instantsearch.telemetry.*

/**
 * Default [TelemetryController] implementation.
 */
internal class DefaultTelemetryController : TelemetryController {

    private val telemetryComponents = mutableListOf<Component>()

    public override fun track(component: Component) {
        telemetryComponents += component
    }

    public override fun track(componentType: ComponentType, componentParams: List<ComponentParam>) {
        telemetryComponents += component {
            type = componentType
            parameters.addAll(componentParams)
        }
    }

    override fun schema(): Schema = schema {
        components.addAll(telemetryComponents)
    }
}
