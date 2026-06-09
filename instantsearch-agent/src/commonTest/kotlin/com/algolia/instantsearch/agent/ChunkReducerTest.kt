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
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
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
    fun toolOutputAvailableWithoutToolNameIsNotDropped() {
        // Agent Studio's ai-sdk-5 stream omits `toolName` on `tool-output-available`
        // (the call is already identified by `toolCallId`). Decoding must not drop it.
        val payload = """{"type":"tool-output-available","toolCallId":"c-1",""" +
            """"output":{"hits":[{"objectID":"1","name":"Laptop"}]}}"""
        val chunk = UIMessageChunk.decode(payload)
        assertNotNull(chunk)
        assertTrue(chunk is UIMessageChunk.ToolOutputAvailable)
        assertEquals("c-1", chunk.toolCallId)
    }

    @Test
    fun toolOutputPreservesNameFromInputStartWhenOutputOmitsIt() {
        var msg = UIMessage(id = "alg_msg_3", role = MessageRole.Assistant)
        val output = buildJsonObject {
            put(
                "hits",
                buildJsonArray {
                    add(buildJsonObject { put("objectID", JsonPrimitive("1")); put("name", JsonPrimitive("Laptop")) })
                },
            )
        }
        val chunks = listOf(
            UIMessageChunk.ToolInputStart("algolia_search_index", "c-1"),
            // `toolName` absent on the wire -> decoded as empty string
            UIMessageChunk.ToolOutputAvailable(toolName = "", toolCallId = "c-1", output = output, preliminary = false),
        )
        for (chunk in chunks) msg = ChunkReducer.apply(chunk, msg)

        val tool = msg.parts.filterIsInstance<UIMessagePart.Tool>().single().part
        assertEquals("algolia_search_index", tool.toolName)
        assertTrue(tool.state is ToolCallState.OutputAvailable)
    }

    @Test
    fun toolOutputDeltaAccumulatesAndParses() {
        var msg = UIMessage(id = "alg_msg_4", role = MessageRole.Assistant)
        val chunks = listOf(
            UIMessageChunk.ToolInputStart("algolia_display_results", "c-9"),
            UIMessageChunk.ToolOutputDelta("c-9", "algolia_display_results", "{\"intro\":\"curated\""),
            UIMessageChunk.ToolOutputDelta("c-9", "algolia_display_results", ",\"groups\":[]}"),
        )
        for (chunk in chunks) msg = ChunkReducer.apply(chunk, msg)

        val tool = msg.parts.filterIsInstance<UIMessagePart.Tool>().single().part
        val state = tool.state
        assertTrue(state is ToolCallState.OutputAvailable, "expected OutputAvailable, got $state")
        assertTrue(state.preliminary)
    }

    @Test
    fun dataSuggestionsChunkBecomesDataPart() {
        // `data-suggestions` (prompt chips) is stored as a generic data part so
        // the ChatStore can derive the suggestions list from it.
        val payload = """{"type":"data-suggestions","data":{"suggestions":["How can I do X?","What about Y?"]}}"""
        val chunk = UIMessageChunk.decode(payload)
        assertNotNull(chunk)
        assertTrue(chunk is UIMessageChunk.Data)
        assertEquals("suggestions", chunk.name)

        var msg = UIMessage(id = "alg_msg_5", role = MessageRole.Assistant)
        msg = ChunkReducer.apply(chunk, msg)
        val part = msg.parts.filterIsInstance<UIMessagePart.Data>().single()
        assertEquals("suggestions", part.name)
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
