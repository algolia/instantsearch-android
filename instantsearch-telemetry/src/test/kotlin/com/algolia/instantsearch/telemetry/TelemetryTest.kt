package com.algolia.instantsearch.telemetry

import kotlin.test.Test
import kotlin.test.assertEquals

public class TelemetryTest {

    @Test
    public fun test() {
        val telemetryController = Telemetry()
        telemetryController.trace(ComponentType.facetList, listOf(ComponentParam.facets, ComponentParam.selectionMode))
        telemetryController.trace(
            ComponentType.hitsSearcher,
            listOf(ComponentParam.client, ComponentParam.indexName, ComponentParam.UNRECOGNIZED)
        )
        telemetryController.trace(ComponentType.hitsSearcher, listOf(ComponentParam.client, ComponentParam.indexName))

        val schema = telemetryController.schema()
        assertEquals(2, schema.componentsCount)
        assertEquals(1, schema.componentsList.count { it.type == ComponentType.hitsSearcher })
        assertEquals(2, schema.componentsList.first { it.type == ComponentType.hitsSearcher }.parametersCount)
    }
}
