package com.algolia.instantsearch.agent.model

import com.algolia.instantsearch.agent.ExperimentalAgentStudioApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * One frame parsed from the SSE stream returned by the Agent Studio
 * completions endpoint.
 *
 * Mirrors `UIMessageChunk` from `instantsearch.js/src/lib/ai-lite/types.ts`.
 * The closed set of variants below covers what the v0.1 [com.algolia.instantsearch.agent.chat.ChatStore]
 * aggregates today; anything else falls into [Unknown] so backend additions
 * don't break parsing.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public sealed interface UIMessageChunk {
    public data class Start(val messageId: String?) : UIMessageChunk
    public object StartStep : UIMessageChunk
    public object FinishStep : UIMessageChunk
    public object Finish : UIMessageChunk
    public object Abort : UIMessageChunk

    public data class TextStart(val id: String) : UIMessageChunk
    public data class TextDelta(val id: String, val delta: String) : UIMessageChunk
    public data class TextEnd(val id: String) : UIMessageChunk

    public data class ReasoningStart(val id: String) : UIMessageChunk
    public data class ReasoningDelta(val id: String, val delta: String) : UIMessageChunk
    public data class ReasoningEnd(val id: String) : UIMessageChunk

    public data class ToolInputStart(val toolName: String, val toolCallId: String) : UIMessageChunk
    public data class ToolInputDelta(val toolName: String, val toolCallId: String, val inputTextDelta: String) : UIMessageChunk
    public data class ToolInputAvailable(val toolName: String, val toolCallId: String, val input: JsonElement) : UIMessageChunk
    public data class ToolOutputAvailable(
        val toolName: String,
        val toolCallId: String,
        val output: JsonElement,
        val preliminary: Boolean,
    ) : UIMessageChunk

    public data class ToolError(
        val toolName: String,
        val toolCallId: String,
        val errorText: String,
        val input: JsonElement?,
    ) : UIMessageChunk

    public data class SourceUrl(val sourceId: String, val url: String, val title: String?) : UIMessageChunk
    public data class File(val url: String, val mediaType: String) : UIMessageChunk

    public data class Data(val name: String, val id: String?, val json: JsonElement) : UIMessageChunk

    public data class Error(val errorText: String) : UIMessageChunk

    public data class Unknown(val typeIdentifier: String, val json: JsonElement) : UIMessageChunk

    public companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        /**
         * Decode a single SSE payload (the JSON after `data:`) into a chunk.
         * Returns `null` if the payload is malformed (callers ignore those,
         * matching the JS [parseJsonEventStream] behavior).
         */
        public fun decode(payload: String): UIMessageChunk? {
            return runCatching { decodeOrThrow(payload) }.getOrNull()
        }

        public fun decodeOrThrow(payload: String): UIMessageChunk {
            val element = json.parseToJsonElement(payload)
            val obj = element.jsonObject
            val type = obj["type"]?.jsonPrimitive?.contentOrNull
                ?: error("Chunk missing `type`")

            fun str(key: String): String? = obj[key]?.jsonPrimitive?.contentOrNull
            fun bool(key: String): Boolean? = obj[key]?.jsonPrimitive?.booleanOrNull
            fun sub(key: String): JsonElement? = obj[key]

            return when (type) {
                "start" -> Start(str("messageId"))
                "start-step" -> StartStep
                "finish-step" -> FinishStep
                "finish" -> Finish
                "abort" -> Abort

                "text-start" -> TextStart(requireNotNull(str("id")))
                "text-delta" -> TextDelta(requireNotNull(str("id")), requireNotNull(str("delta")))
                "text-end" -> TextEnd(requireNotNull(str("id")))

                "reasoning-start" -> ReasoningStart(requireNotNull(str("id")))
                "reasoning-delta" -> ReasoningDelta(requireNotNull(str("id")), requireNotNull(str("delta")))
                "reasoning-end" -> ReasoningEnd(requireNotNull(str("id")))

                "tool-input-start" -> ToolInputStart(
                    toolName = requireNotNull(str("toolName")),
                    toolCallId = requireNotNull(str("toolCallId")),
                )
                "tool-input-delta" -> ToolInputDelta(
                    toolName = requireNotNull(str("toolName")),
                    toolCallId = requireNotNull(str("toolCallId")),
                    inputTextDelta = requireNotNull(str("inputTextDelta")),
                )
                "tool-input-available" -> ToolInputAvailable(
                    toolName = requireNotNull(str("toolName")),
                    toolCallId = requireNotNull(str("toolCallId")),
                    input = requireNotNull(sub("input")),
                )
                "tool-output-available" -> ToolOutputAvailable(
                    toolName = requireNotNull(str("toolName")),
                    toolCallId = requireNotNull(str("toolCallId")),
                    output = requireNotNull(sub("output")),
                    preliminary = bool("preliminary") ?: false,
                )
                "tool-error" -> ToolError(
                    toolName = requireNotNull(str("toolName")),
                    toolCallId = requireNotNull(str("toolCallId")),
                    errorText = requireNotNull(str("errorText")),
                    input = sub("input"),
                )

                "source-url" -> SourceUrl(
                    sourceId = requireNotNull(str("sourceId")),
                    url = requireNotNull(str("url")),
                    title = str("title"),
                )
                "file" -> File(
                    url = requireNotNull(str("url")),
                    mediaType = requireNotNull(str("mediaType")),
                )
                "error" -> Error(str("errorText") ?: "Unknown error")
                else -> {
                    if (type.startsWith("data-")) {
                        Data(
                            name = type.removePrefix("data-"),
                            id = str("id"),
                            json = sub("data") ?: JsonObject(emptyMap()),
                        )
                    } else {
                        Unknown(type, element)
                    }
                }
            }
        }
    }
}
