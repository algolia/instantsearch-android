package com.algolia.instantsearch.agentstudio.transport

import com.algolia.instantsearch.agentstudio.model.UIMessageChunk
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Convert a streaming HTTP response body into a [Flow] of [UIMessageChunk]s.
 *
 * The wire format is the AI SDK 5 SSE protocol:
 *
 *     data: <json>      // a UIMessageChunk
 *     data: [DONE]      // end-of-stream sentinel
 *     <empty line>      // SSE keep-alive, ignored
 *     event: ...        // ignored
 *     id: ...           // ignored
 *
 * Mirrors [parseJsonEventStream] from
 * `instantsearch.js/src/lib/ai-lite/stream-parser.ts`.
 */
public object SseEventStream {

    public fun fromChannel(channel: ByteReadChannel): Flow<UIMessageChunk> = flow {
        while (true) {
            val line = channel.readUTF8Line() ?: break
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            val payload = extractJsonPayload(trimmed) ?: continue
            if (payload == "[DONE]") break

            val chunk = UIMessageChunk.decode(payload) ?: continue
            emit(chunk)
        }
    }

    internal fun extractJsonPayload(line: String): String? = when {
        line.startsWith("data:") -> line.removePrefix("data:").trim()
        line.startsWith("{") -> line
        else -> null
    }
}
