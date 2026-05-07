package com.algolia.instantsearch.agentstudio.transport

/**
 * Builds the Agent Studio completions URL for a given Algolia application
 * and agent. Mirrors the URL shape used by `connectChat` in
 * `instantsearch.js/src/connectors/chat/connectChat.ts`:
 *
 *     https://{appId}.algolia.net/agent-studio/1/agents/{agentId}/completions
 *
 * We always request `compatibilityMode=ai-sdk-5`. Streaming is opt-in through
 * `stream=true` (the default for [com.algolia.instantsearch.agentstudio.chat.ChatStore]).
 */
public data class AgentStudioEndpoint(
    public val appId: String,
    public val agentId: String,
    public val host: String = "$appId.algolia.net",
) {

    /**
     * @param stream when `true`, the server streams chunks as SSE.
     * @param cache when `false`, bypasses the server-side cache (used by
     *  `regenerate-message`).
     */
    public fun completionsUrl(stream: Boolean = true, cache: Boolean = true): String {
        val query = buildList {
            add("compatibilityMode" to "ai-sdk-5")
            add("stream" to if (stream) "true" else "false")
            if (!cache) add("cache" to "false")
        }.joinToString("&") { "${it.first}=${it.second}" }

        return "https://$host/agent-studio/1/agents/$agentId/completions?$query"
    }
}
