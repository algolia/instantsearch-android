package com.algolia.instantsearch.agent.model

import com.algolia.instantsearch.agent.ExperimentalAgentStudioApi

/**
 * State of the chat lifecycle, mirroring the JS `ChatStatus` enum from
 * `instantsearch.js/src/lib/ai-lite/types.ts`.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public enum class ChatStatus { Submitted, Streaming, Ready, Error }

/**
 * Errors surfaced by the agent studio client.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public sealed class AgentStudioException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    public class Http(public val status: Int, public val body: String?) :
        AgentStudioException("HTTP $status${body?.let { " — $it" } ?: ""}")
    public class MalformedChunk(payload: String) : AgentStudioException("Malformed chunk: $payload")
    public class StreamClosed : AgentStudioException("Stream closed unexpectedly")
    public class Underlying(message: String, cause: Throwable? = null) : AgentStudioException(message, cause)
}
