package com.algolia.instantsearch.telemetry

import kotlin.test.Test
import kotlin.test.assertEquals

class TelemetryTest {

    @Test
    fun schemaBuildTest() {
        val telemetry = Telemetry()
        telemetry.trace(ComponentType.FacetList, listOf(ComponentParam.Facets, ComponentParam.SelectionMode))
        telemetry.trace(
            ComponentType.HitsSearcher,
            listOf(ComponentParam.Client, ComponentParam.IndexName, ComponentParam.Undefined)
        )
        telemetry.trace(ComponentType.HitsSearcher, listOf(ComponentParam.Client, ComponentParam.IndexName))

        val schema = telemetry.schema()
        assertEquals(2, schema.components.size)
        assertEquals(1, schema.components.count { it.type == ComponentType.HitsSearcher })
        assertEquals(2, schema.components.first { it.type == ComponentType.HitsSearcher }.parameters.size)
    }
}
