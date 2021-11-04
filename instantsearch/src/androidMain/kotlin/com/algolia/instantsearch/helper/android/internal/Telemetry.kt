package com.algolia.instantsearch.helper.android.internal

import com.algolia.instantsearch.telemetry.Telemetry
import com.algolia.instantsearch.telemetry.component

internal object Telemetry {

    private val components = mutableListOf<Telemetry.Component>()

    fun track(component: Telemetry.Component) {
        components += component
    }

    fun track(componentType: Telemetry.ComponentType, componentParams: List<Telemetry.ComponentParam>) {
        components += component {
            type = componentType
            parameters.addAll(componentParams)
        }
    }
}
