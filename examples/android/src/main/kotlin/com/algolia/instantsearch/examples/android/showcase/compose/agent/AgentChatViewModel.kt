package com.algolia.instantsearch.examples.android.showcase.compose.agent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.instantsearch.agent.chat.ChatStore
import com.algolia.instantsearch.agent.transport.AgentStudioTransport

/**
 * Owns the [ChatStore] for the Agent Studio showcase and rebuilds it whenever
 * the entered agent id changes.
 *
 * Uses the public demo `latency` credentials shared by the other showcases.
 * In a real app, pass a **search-only** API key — never an admin key.
 */
class AgentChatViewModel : ViewModel() {

    private var agentId: String = ""

    var store: ChatStore = buildStore(agentId)
        private set

    fun setAgentId(value: String) {
        if (value == agentId) return
        agentId = value
        store.clear()
        store = buildStore(value)
    }

    fun send(text: String) {
        if (agentId.isBlank()) return
        store.send(text)
    }

    fun stop() = store.stop()

    private fun buildStore(agentId: String): ChatStore {
        val transport = AgentStudioTransport.fromCredentials(
            appId = APP_ID,
            apiKey = SEARCH_API_KEY,
            agentId = agentId.ifBlank { "unset" },
        )
        return ChatStore(transport = transport, scope = viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        store.stop()
    }

    private companion object {
        const val APP_ID = "latency"
        const val SEARCH_API_KEY = "1f6fd3a6fb973cb08419fe7d288fa4db"
    }
}
