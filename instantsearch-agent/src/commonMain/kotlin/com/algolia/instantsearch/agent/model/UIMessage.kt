package com.algolia.instantsearch.agent.model

import com.algolia.instantsearch.agent.ExperimentalAgentStudioApi
import kotlinx.serialization.json.JsonElement

/**
 * Role of a message in an Agent Studio conversation.
 *
 * Mirrors the JS `UIMessage['role']` from
 * `instantsearch.js/src/lib/ai-lite/types.ts`.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public enum class MessageRole {
    System, User, Assistant;

    public val wireValue: String
        get() = when (this) {
            System -> "system"
            User -> "user"
            Assistant -> "assistant"
        }

    public companion object {
        public fun fromWire(value: String): MessageRole = when (value) {
            "system" -> System
            "user" -> User
            "assistant" -> Assistant
            else -> error("Unknown role: $value")
        }
    }
}

/**
 * State of a streaming part (text, reasoning, …) inside a message.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public enum class PartState { Streaming, Done }

/**
 * A single part of an assembled [UIMessage].
 *
 * Mirrors the JS `UIMessagePart` discriminated union; modeled here as a sealed
 * hierarchy with an [Unknown] fallback so newly-introduced variants don't
 * crash old clients.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public sealed interface UIMessagePart {
    public data class Text(val id: String?, val text: String, val state: PartState?) : UIMessagePart
    public data class Reasoning(val id: String?, val text: String, val state: PartState?) : UIMessagePart
    public data class SourceUrl(val sourceId: String, val url: String, val title: String?) : UIMessagePart
    public data class File(val mediaType: String, val url: String, val filename: String?) : UIMessagePart
    public object StepStart : UIMessagePart

    /** A tool invocation, keyed by [Tool.toolCallId]. */
    public data class Tool(val part: ToolUIPart) : UIMessagePart

    /** Custom `data-<name>` part. Payload kept as raw JSON. */
    public data class Data(val name: String, val id: String?, val json: JsonElement) : UIMessagePart

    /** Forward-compat fallback for unknown chunk types. */
    public data class Unknown(val typeIdentifier: String, val json: JsonElement) : UIMessagePart
}

/**
 * Lifecycle of a tool call. Mirrors the AI SDK 5 `tool-input-*` /
 * `tool-output-*` chunk progression.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public sealed interface ToolCallState {
    public data class InputStreaming(val partial: JsonElement?) : ToolCallState
    public data class InputAvailable(val input: JsonElement) : ToolCallState
    public data class OutputAvailable(
        val input: JsonElement,
        val output: JsonElement,
        val preliminary: Boolean,
    ) : ToolCallState
    public data class OutputError(val input: JsonElement?, val errorText: String) : ToolCallState
}

/**
 * A single tool invocation embedded in an assistant message.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public data class ToolUIPart(
    val toolName: String,
    val toolCallId: String,
    val state: ToolCallState,
)

/**
 * An assembled message ready to be rendered by the host app.
 *
 * Metadata is intentionally typeless in v0.1; callers that need to decode it
 * can do so from the original `start` chunk. We keep it a `JsonElement?` to
 * stay generic without forcing a type parameter through the whole API.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public data class UIMessage(
    val id: String,
    val role: MessageRole,
    val parts: List<UIMessagePart> = emptyList(),
    val metadata: JsonElement? = null,
) {
    /** Concatenated text from every [UIMessagePart.Text] part. */
    val plainText: String
        get() = parts.asSequence()
            .filterIsInstance<UIMessagePart.Text>()
            .joinToString(separator = "") { it.text }
}
