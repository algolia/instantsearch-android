package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.telemetry.ComponentParam.Client
import com.algolia.instantsearch.telemetry.ComponentParam.Facets
import com.algolia.instantsearch.telemetry.ComponentParam.IndexName
import com.algolia.instantsearch.telemetry.ComponentParam.SelectionMode
import com.algolia.instantsearch.telemetry.ComponentParam.Undefined
import com.algolia.instantsearch.telemetry.ComponentType.FacetList
import com.algolia.instantsearch.telemetry.ComponentType.HitsSearcher
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(Internal::class)
class TelemetryTest {

    @Test
    fun schemaBuildTest() {
        val telemetry = Telemetry()
        telemetry.traceConnector(FacetList, setOf(Facets, SelectionMode))
        telemetry.trace(HitsSearcher, setOf(Client, IndexName, Undefined))
        telemetry.traceConnector(HitsSearcher, setOf(Client, IndexName))

        val schema = telemetry.schema()
        assertEquals(2, schema.components.size)
        assertEquals(1, schema.components.count { it.type == HitsSearcher })
        assertEquals(2, schema.components.first { it.type == HitsSearcher }.parameters.size)
        assertEquals(true, schema.components.first { it.type == HitsSearcher }.isConnector)
    }
}
