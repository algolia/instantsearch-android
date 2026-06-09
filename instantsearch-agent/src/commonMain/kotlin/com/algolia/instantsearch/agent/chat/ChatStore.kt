package com.algolia.instantsearch.agent.chat

import com.algolia.instantsearch.agent.ExperimentalAgentStudioApi
import com.algolia.instantsearch.agent.model.AgentStudioException
import com.algolia.instantsearch.agent.model.ChatStatus
import com.algolia.instantsearch.agent.model.MessageRole
import com.algolia.instantsearch.agent.model.PartState
import com.algolia.instantsearch.agent.model.UIMessage
import com.algolia.instantsearch.agent.model.UIMessageChunk
import com.algolia.instantsearch.agent.model.UIMessagePart
import com.algolia.instantsearch.agent.transport.AgentStudioRequest
import com.algolia.instantsearch.agent.transport.AgentStudioTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlin.random.Random

/**
 * Native counterpart of the JS `Chat` class
 * (`instantsearch.js/src/lib/chat/chat.ts`).
 *
 * Aggregates streamed [UIMessageChunk]s into a list of [UIMessage]s and
 * exposes [messages], [status], and [error] as [StateFlow]s so Compose / View
 * code can observe them.
 *
 * @param scope coroutine scope owning streaming jobs. Pass `viewModelScope` in
 *  Android, or a custom scope tied to the lifecycle of your screen.
 * @param conversationId pass a stable id (prefixed `alg_cnv_` per Algolia
 *  conventions) to enable server-side conversation persistence; pass `null`
 *  to let the store generate one.
 *
 * This is **experimental** API — see [ExperimentalAgentStudioApi].
 */
@ExperimentalAgentStudioApi
public class ChatStore(
    private val transport: AgentStudioTransport,
    private val scope: CoroutineScope,
    conversationId: String? = null,
    initialMessages: List<UIMessage> = emptyList(),
    private val idGenerator: () -> String = { "alg_msg_" + randomId() },
) {
    public val conversationId: String = conversationId ?: ("alg_cnv_" + randomId())

    private val _messages = MutableStateFlow(initialMessages)
    public val messages: StateFlow<List<UIMessage>> = _messages.asStateFlow()

    private val _status = MutableStateFlow(ChatStatus.Ready)
    public val status: StateFlow<ChatStatus> = _status.asStateFlow()

    private val _error = MutableStateFlow<AgentStudioException?>(null)
    public val error: StateFlow<AgentStudioException?> = _error.asStateFlow()

    /**
     * Prompt suggestions ("what to ask next") streamed by the agent as a
     * `data-suggestions` part. Derived from the last assistant message, matching
     * the web `connectChat` behavior. Hosts typically show these as tappable
     * chips while [status] is [ChatStatus.Ready].
     */
    public val suggestions: StateFlow<List<String>> = messages
        .map { extractSuggestions(it) }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private var streamingJob: Job? = null
    private var assistantIndex: Int? = null

    /**
     * Send a user text message and start streaming the assistant response.
     * Cancels any in-flight request.
     */
    public fun send(text: String): Job {
        val userMessage = UIMessage(
            id = idGenerator(),
            role = MessageRole.User,
            parts = listOf(UIMessagePart.Text(id = null, text = text, state = PartState.Done)),
        )
        _messages.update { it + userMessage }
        return startStream(AgentStudioRequest.Trigger.SubmitMessage)
    }

    /** Re-issue the last user message, replacing the trailing assistant message (if any). */
    public fun regenerate(): Job {
        _messages.update { current ->
            if (current.lastOrNull()?.role == MessageRole.Assistant) current.dropLast(1) else current
        }
        return startStream(AgentStudioRequest.Trigger.RegenerateMessage)
    }

    /** Cancel any in-flight streaming request. Status returns to [ChatStatus.Ready]. */
    public fun stop() {
        streamingJob?.cancel()
        streamingJob = null
        assistantIndex = null
        if (_status.value != ChatStatus.Error) {
            _status.value = ChatStatus.Ready
        }
    }

    /** Wipe local conversation state. Does not call any server endpoint. */
    public fun clear() {
        stop()
        _messages.value = emptyList()
        _error.value = null
        _status.value = ChatStatus.Ready
    }

    public fun clearError() {
        _error.value = null
        if (_status.value == ChatStatus.Error) _status.value = ChatStatus.Ready
    }

    private fun startStream(trigger: AgentStudioRequest.Trigger): Job {
        streamingJob?.cancel()
        _error.value = null
        _status.value = ChatStatus.Submitted
        assistantIndex = null

        val wireMessages = _messages.value.mapNotNull { msg ->
            val text = msg.plainText
            if (text.isEmpty()) null
            else AgentStudioRequest.WireMessage(id = msg.id, role = msg.role, text = text)
        }

        val request = AgentStudioRequest(
            conversationId = conversationId,
            messages = wireMessages,
            trigger = trigger,
        )

        return scope.launch {
            try {
                transport.sendMessages(request).collect { chunk -> handleChunk(chunk) }
                _status.value = ChatStatus.Ready
            } catch (cancellation: kotlinx.coroutines.CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                _status.value = ChatStatus.Error
                _error.value = (throwable as? AgentStudioException)
                    ?: AgentStudioException.Underlying(
                        message = throwable.message ?: throwable::class.simpleName.orEmpty(),
                        cause = throwable,
                    )
            }
        }.also { streamingJob = it }
    }

    private fun handleChunk(chunk: UIMessageChunk) {
        when (chunk) {
            is UIMessageChunk.Start -> {
                val id = chunk.messageId ?: idGenerator()
                _messages.update { it + UIMessage(id = id, role = MessageRole.Assistant) }
                assistantIndex = _messages.value.lastIndex
                _status.value = ChatStatus.Streaming
            }
            is UIMessageChunk.Error -> {
                _status.value = ChatStatus.Error
                _error.value = AgentStudioException.Underlying(chunk.errorText)
            }
            else -> {
                ensureAssistantMessage()
                val index = assistantIndex ?: return
                _messages.update { current ->
                    current.toMutableList().apply { set(index, ChunkReducer.apply(chunk, current[index])) }
                }
                if (_status.value == ChatStatus.Submitted) _status.value = ChatStatus.Streaming
            }
        }
    }

    private fun ensureAssistantMessage() {
        if (assistantIndex == null) {
            _messages.update { it + UIMessage(id = idGenerator(), role = MessageRole.Assistant) }
            assistantIndex = _messages.value.lastIndex
        }
    }

    public companion object {
        private fun randomId(): String {
            val bytes = ByteArray(16).also { Random.nextBytes(it) }
            return bytes.joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
        }

        private fun extractSuggestions(messages: List<UIMessage>): List<String> {
            val message = messages.lastOrNull { it.role == MessageRole.Assistant } ?: return emptyList()
            val part = message.parts
                .filterIsInstance<UIMessagePart.Data>()
                .firstOrNull { it.name == "suggestions" } ?: return emptyList()
            val array = (part.json as? JsonObject)?.get("suggestions") as? JsonArray ?: return emptyList()
            return array.mapNotNull { it.jsonPrimitive.contentOrNull }
        }
    }
}

