package com.algolia.instantsearch.agent.chat

import com.algolia.instantsearch.agent.model.PartState
import com.algolia.instantsearch.agent.model.ToolCallState
import com.algolia.instantsearch.agent.model.ToolUIPart
import com.algolia.instantsearch.agent.model.UIMessage
import com.algolia.instantsearch.agent.model.UIMessageChunk
import com.algolia.instantsearch.agent.model.UIMessagePart
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

/**
 * Reduces a [UIMessageChunk] onto an existing assistant [UIMessage].
 *
 * Native equivalent of the chunk-handling switch inside `AbstractChat.processStreamChunk`
 * in `instantsearch.js/src/lib/ai-lite/abstract-chat.ts`.
 */
internal object ChunkReducer {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Parse accumulated `data-tool-output-delta` text into a preliminary
     * [ToolCallState.OutputAvailable]. While the JSON is still incomplete we keep
     * the part in [ToolCallState.InputAvailable] so the UI shows a running state.
     */
    private fun toolStateFromRawOutput(raw: String, previousInput: JsonElement): ToolCallState {
        val parsed: JsonElement? = runCatching { json.parseToJsonElement(raw) }.getOrNull()
        return if (parsed != null) {
            ToolCallState.OutputAvailable(input = previousInput, output = parsed, preliminary = true)
        } else {
            ToolCallState.InputAvailable(input = previousInput)
        }
    }

    fun apply(chunk: UIMessageChunk, message: UIMessage): UIMessage {
        return when (chunk) {
            is UIMessageChunk.Start, is UIMessageChunk.Error -> message
            UIMessageChunk.StartStep -> message.copy(parts = message.parts + UIMessagePart.StepStart)
            UIMessageChunk.FinishStep, UIMessageChunk.Finish, UIMessageChunk.Abort -> {
                message.copy(parts = message.parts.map { part ->
                    when (part) {
                        is UIMessagePart.Text -> if (part.state == PartState.Streaming) part.copy(state = PartState.Done) else part
                        is UIMessagePart.Reasoning -> if (part.state == PartState.Streaming) part.copy(state = PartState.Done) else part
                        else -> part
                    }
                })
            }

            is UIMessageChunk.TextStart -> message.copy(
                parts = message.parts + UIMessagePart.Text(id = chunk.id, text = "", state = PartState.Streaming),
            )
            is UIMessageChunk.TextDelta -> appendToLastText(message, chunk.id, chunk.delta)
            is UIMessageChunk.TextEnd -> updateLastText(message, chunk.id) { it.copy(state = PartState.Done) }

            is UIMessageChunk.ReasoningStart -> message.copy(
                parts = message.parts + UIMessagePart.Reasoning(id = chunk.id, text = "", state = PartState.Streaming),
            )
            is UIMessageChunk.ReasoningDelta -> appendToLastReasoning(message, chunk.id, chunk.delta)
            is UIMessageChunk.ReasoningEnd -> updateLastReasoning(message, chunk.id) { it.copy(state = PartState.Done) }

            is UIMessageChunk.ToolInputStart -> upsertTool(message, chunk.toolCallId,
                insert = { ToolUIPart(chunk.toolName, chunk.toolCallId, ToolCallState.InputStreaming(null)) },
                update = { it.copy(state = ToolCallState.InputStreaming(null)) },
            )
            is UIMessageChunk.ToolInputDelta -> message
            is UIMessageChunk.ToolInputAvailable -> upsertTool(message, chunk.toolCallId,
                insert = { ToolUIPart(chunk.toolName, chunk.toolCallId, ToolCallState.InputAvailable(chunk.input)) },
                update = { it.copy(state = ToolCallState.InputAvailable(chunk.input)) },
            )
            is UIMessageChunk.ToolOutputAvailable -> upsertTool(message, chunk.toolCallId,
                insert = {
                    ToolUIPart(
                        chunk.toolName, chunk.toolCallId,
                        ToolCallState.OutputAvailable(input = JsonNull, output = chunk.output, preliminary = chunk.preliminary),
                    )
                },
                update = { existing ->
                    val previousInput = (existing.state as? ToolCallState.InputAvailable)?.input
                        ?: (existing.state as? ToolCallState.OutputAvailable)?.input
                        ?: JsonNull
                    existing.copy(state = ToolCallState.OutputAvailable(previousInput, chunk.output, chunk.preliminary))
                },
            )
            is UIMessageChunk.ToolError -> upsertTool(message, chunk.toolCallId,
                insert = {
                    ToolUIPart(chunk.toolName, chunk.toolCallId, ToolCallState.OutputError(chunk.input, chunk.errorText))
                },
                update = { it.copy(state = ToolCallState.OutputError(chunk.input, chunk.errorText)) },
            )
            is UIMessageChunk.ToolOutputDelta -> upsertTool(message, chunk.toolCallId,
                insert = {
                    val raw = chunk.delta
                    ToolUIPart(
                        toolName = chunk.toolName ?: "",
                        toolCallId = chunk.toolCallId,
                        state = toolStateFromRawOutput(raw, previousInput = JsonNull),
                        rawOutput = raw,
                    )
                },
                update = { existing ->
                    val raw = existing.rawOutput + chunk.delta
                    val previousInput = (existing.state as? ToolCallState.InputAvailable)?.input
                        ?: (existing.state as? ToolCallState.OutputAvailable)?.input
                        ?: JsonNull
                    existing.copy(state = toolStateFromRawOutput(raw, previousInput), rawOutput = raw)
                },
            )

            is UIMessageChunk.SourceUrl -> message.copy(
                parts = message.parts + UIMessagePart.SourceUrl(chunk.sourceId, chunk.url, chunk.title),
            )
            is UIMessageChunk.File -> message.copy(
                parts = message.parts + UIMessagePart.File(chunk.mediaType, chunk.url, filename = null),
            )
            is UIMessageChunk.Data -> message.copy(
                parts = message.parts + UIMessagePart.Data(chunk.name, chunk.id, chunk.json),
            )
            is UIMessageChunk.Unknown -> message.copy(
                parts = message.parts + UIMessagePart.Unknown(chunk.typeIdentifier, chunk.json),
            )
        }
    }

    private fun appendToLastText(message: UIMessage, id: String, delta: String): UIMessage {
        val index = message.parts.indexOfLast { it is UIMessagePart.Text && it.id == id }
        return if (index >= 0) {
            val current = message.parts[index] as UIMessagePart.Text
            val updated = current.copy(text = current.text + delta, state = PartState.Streaming)
            message.copy(parts = message.parts.toMutableList().apply { set(index, updated) })
        } else {
            message.copy(parts = message.parts + UIMessagePart.Text(id, delta, PartState.Streaming))
        }
    }

    private fun updateLastText(message: UIMessage, id: String, transform: (UIMessagePart.Text) -> UIMessagePart.Text): UIMessage {
        val index = message.parts.indexOfLast { it is UIMessagePart.Text && it.id == id }
        if (index < 0) return message
        val updated = transform(message.parts[index] as UIMessagePart.Text)
        return message.copy(parts = message.parts.toMutableList().apply { set(index, updated) })
    }

    private fun appendToLastReasoning(message: UIMessage, id: String, delta: String): UIMessage {
        val index = message.parts.indexOfLast { it is UIMessagePart.Reasoning && it.id == id }
        return if (index >= 0) {
            val current = message.parts[index] as UIMessagePart.Reasoning
            val updated = current.copy(text = current.text + delta, state = PartState.Streaming)
            message.copy(parts = message.parts.toMutableList().apply { set(index, updated) })
        } else {
            message.copy(parts = message.parts + UIMessagePart.Reasoning(id, delta, PartState.Streaming))
        }
    }

    private fun updateLastReasoning(
        message: UIMessage,
        id: String,
        transform: (UIMessagePart.Reasoning) -> UIMessagePart.Reasoning,
    ): UIMessage {
        val index = message.parts.indexOfLast { it is UIMessagePart.Reasoning && it.id == id }
        if (index < 0) return message
        val updated = transform(message.parts[index] as UIMessagePart.Reasoning)
        return message.copy(parts = message.parts.toMutableList().apply { set(index, updated) })
    }

    private fun upsertTool(
        message: UIMessage,
        toolCallId: String,
        insert: () -> ToolUIPart,
        update: (ToolUIPart) -> ToolUIPart,
    ): UIMessage {
        val index = message.parts.indexOfFirst { it is UIMessagePart.Tool && it.part.toolCallId == toolCallId }
        return if (index >= 0) {
            val current = (message.parts[index] as UIMessagePart.Tool).part
            val updatedPart = UIMessagePart.Tool(update(current))
            message.copy(parts = message.parts.toMutableList().apply { set(index, updatedPart) })
        } else {
            message.copy(parts = message.parts + UIMessagePart.Tool(insert()))
        }
    }
}
