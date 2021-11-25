package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.telemetry.ComponentParam.Client
import com.algolia.instantsearch.telemetry.ComponentParam.Facets
import com.algolia.instantsearch.telemetry.ComponentParam.IndexName
import com.algolia.instantsearch.telemetry.ComponentParam.SelectionMode
import com.algolia.instantsearch.telemetry.ComponentType.FacetList
import com.algolia.instantsearch.telemetry.ComponentType.HitsSearcher
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(Internal::class)
class SerializationTest {

    @Test
    fun serializationTest() {
        val telemetry = Telemetry()
        telemetry.traceViewModel(FacetList, listOf(Facets, SelectionMode))
        telemetry.traceViewModel(HitsSearcher, listOf(Client, IndexName))
        val schema = telemetry.schema()
        val bytes = ProtoBuf.encodeToByteArray(schema)
        val decoded = ProtoBuf.decodeFromByteArray<Schema>(bytes)

        val hex = "{E2}+{0C}{C0}%{08}{C8}%{07}{C8}%{18}{D0}%{00}{E2}+{0C}{C0}%{01}{C8}%{06}{C8}%{0D}{D0}%{00}"
        assertEquals(hex, bytes.toAsciiHexString())
        assertEquals(schema, decoded)
    }

    private fun ByteArray.toAsciiHexString() = joinToString("") {
        when (it) {
            in 32..127 -> it.toInt().toChar().toString()
            else -> "{${it.toUByte().toString(16).padStart(2, '0').uppercase()}}"
        }
    }
}
