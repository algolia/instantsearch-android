package com.algolia.instantsearch.migration2to3

import io.ktor.util.GZip
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.runBlocking

internal object Gzip : (String) -> ByteArray {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun invoke(input: String): ByteArray {
        return runBlocking {
            GZip.run {
                encode(ByteReadChannel(input)).toByteArray()
            }
        }
    }
}

public suspend fun ByteReadChannel.toByteArray(): ByteArray = readRemaining().readBytes()
