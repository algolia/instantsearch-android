package com.algolia.instantsearch.agent.transport

import com.algolia.instantsearch.agent.ExperimentalAgentStudioApi
import com.algolia.instantsearch.agent.model.AgentStudioException
import com.algolia.instantsearch.agent.model.MessageRole
import com.algolia.instantsearch.agent.model.UIMessageChunk
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * What we send in the request body.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public data class AgentStudioRequest(
    public val conversationId: String?,
    public val messages: List<WireMessage>,
    public val trigger: Trigger = Trigger.SubmitMessage,
    /** Extra top-level body fields (e.g. `algolia.searchParameters`). */
    public val extra: Map<String, JsonElement> = emptyMap(),
) {
    public enum class Trigger(public val wireValue: String) {
        SubmitMessage("submit-message"),
        RegenerateMessage("regenerate-message");
    }

    /**
     * On-the-wire shape of a message. We keep it separate from the assembled
     * [com.algolia.instantsearch.agent.model.UIMessage] because the wire
     * format only carries text parts on the user side.
     */
    public data class WireMessage(
        public val id: String,
        public val role: MessageRole,
        public val text: String,
    )
}

/**
 * HTTP transport for the Agent Studio completions endpoint.
 *
 * Mirrors [DefaultChatTransport] from
 * `instantsearch.js/src/lib/ai-lite/transport.ts`.
 *
 * @param httpClient supply your own (e.g. with a configured [HttpClient]
 *  engine, logging, or auth plugin) or use the default OkHttp engine.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public class AgentStudioTransport(
    public val endpoint: AgentStudioEndpoint,
    private val apiKey: String,
    private val userAgent: String? = null,
    private val httpClient: HttpClient = HttpClient(),
) {

    public fun sendMessages(request: AgentStudioRequest): Flow<UIMessageChunk> = flow {
        val url = endpoint.completionsUrl(
            stream = true,
            cache = request.trigger != AgentStudioRequest.Trigger.RegenerateMessage,
        )
        val body = encodeBody(request)

        val statement: HttpStatement = httpClient.preparePost(url) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "text/event-stream")
                append("x-algolia-application-id", endpoint.appId)
                append("x-algolia-api-key", apiKey)
                userAgent?.let { append("x-algolia-agent", it) }
            }
            setBody(body)
        }

        statement.execute { response ->
            if (response.status != HttpStatusCode.OK) {
                throw AgentStudioException.Http(response.status.value, body = null)
            }
            SseEventStream.fromChannel(response.bodyAsChannel()).collect { emit(it) }
        }
    }

    private fun encodeBody(request: AgentStudioRequest): String {
        val obj = buildJsonObject {
            request.conversationId?.let { put("id", it) }
            put("trigger", request.trigger.wireValue)
            put("messages", buildJsonArray {
                request.messages.forEach { msg ->
                    add(buildJsonObject {
                        put("id", msg.id)
                        put("role", msg.role.wireValue)
                        put("parts", buildJsonArray {
                            add(buildJsonObject {
                                put("type", "text")
                                put("text", msg.text)
                            })
                        })
                    })
                }
            })
            request.extra.forEach { (key, value) -> put(key, value) }
        }
        return Json.encodeToString(JsonObject.serializer(), obj)
    }

    public companion object {
        /**
         * Convenience factory that builds a transport from raw Algolia
         * credentials. Pass values from your existing `ClientSearch`
         * (`applicationId.raw`, `apiKey.raw`).
         *
         * Important: use a **search-only** API key. Never embed admin keys
         * in a shipping mobile app.
         */
        public fun fromCredentials(
            appId: String,
            apiKey: String,
            agentId: String,
            userAgent: String? = null,
            httpClient: HttpClient = HttpClient(),
        ): AgentStudioTransport = AgentStudioTransport(
            endpoint = AgentStudioEndpoint(appId = appId, agentId = agentId),
            apiKey = apiKey,
            userAgent = userAgent,
            httpClient = httpClient,
        )
    }
}
