package com.algolia.instantsearch.agent

import com.algolia.instantsearch.agent.chat.ChunkReducer
import com.algolia.instantsearch.agent.model.MessageRole
import com.algolia.instantsearch.agent.model.PartState
import com.algolia.instantsearch.agent.model.ToolCallState
import com.algolia.instantsearch.agent.model.UIMessage
import com.algolia.instantsearch.agent.model.UIMessageChunk
import com.algolia.instantsearch.agent.model.UIMessagePart
import com.algolia.instantsearch.agent.transport.AgentStudioEndpoint
import com.algolia.instantsearch.agent.transport.SseEventStream
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChunkReducerTest {
    @Test
    fun textStreamProducesConcatenatedPart() {
        var msg = UIMessage(id = "alg_msg_1", role = MessageRole.Assistant)
        val chunks = listOf(
            UIMessageChunk.StartStep,
            UIMessageChunk.TextStart("t-1"),
            UIMessageChunk.TextDelta("t-1", "Hello "),
            UIMessageChunk.TextDelta("t-1", "world"),
            UIMessageChunk.TextEnd("t-1"),
            UIMessageChunk.FinishStep,
            UIMessageChunk.Finish,
        )
        for (chunk in chunks) {
            msg = ChunkReducer.apply(chunk, msg)
        }
        assertEquals("Hello world", msg.plainText)
        val text = msg.parts.filterIsInstance<UIMessagePart.Text>().first()
        assertEquals(PartState.Done, text.state)
    }

    @Test
    fun toolLifecycleEndsWithOutputAvailable() {
        var msg = UIMessage(id = "alg_msg_2", role = MessageRole.Assistant)
        val input = buildJsonObject { put("query", JsonPrimitive("red shoes")) }
        val output = buildJsonObject { put("hits", JsonNull) }
        val chunks = listOf(
            UIMessageChunk.ToolInputStart("algolia_search_index", "c-1"),
            UIMessageChunk.ToolInputAvailable("algolia_search_index", "c-1", input),
            UIMessageChunk.ToolOutputAvailable("algolia_search_index", "c-1", output, false),
        )
        for (chunk in chunks) msg = ChunkReducer.apply(chunk, msg)

        val tool = msg.parts.filterIsInstance<UIMessagePart.Tool>().first().part
        val state = tool.state
        assertTrue(state is ToolCallState.OutputAvailable, "expected OutputAvailable, got $state")
        assertEquals(output, state.output)
    }

    @Test
    fun sseExtractionIgnoresKeepalivesAndDoneSentinel() {
        assertEquals("{\"type\":\"finish\"}", SseEventStream.extractJsonPayload("data: {\"type\":\"finish\"}"))
        assertEquals("[DONE]", SseEventStream.extractJsonPayload("data: [DONE]"))
        assertNull(SseEventStream.extractJsonPayload("event: ping"))
        assertNull(SseEventStream.extractJsonPayload("id: 123"))
    }

    @Test
    fun endpointBuildsExpectedUrl() {
        val endpoint = AgentStudioEndpoint(appId = "ABC123", agentId = "shopping-assistant")
        assertEquals(
            "https://ABC123.algolia.net/agent-studio/1/agents/shopping-assistant/completions" +
                "?compatibilityMode=ai-sdk-5&stream=true",
            endpoint.completionsUrl(stream = true, cache = true),
        )
    }

    @Test
    fun chunkDecodeHandlesUnknownTypeAsFallback() {
        val chunk = UIMessageChunk.decode("""{"type":"future-feature","payload":42}""")
        assertNotNull(chunk)
        assertTrue(chunk is UIMessageChunk.Unknown)
        assertEquals("future-feature", chunk.typeIdentifier)
    }
}
