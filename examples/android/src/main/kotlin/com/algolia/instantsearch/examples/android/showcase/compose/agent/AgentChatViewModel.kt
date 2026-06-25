package com.algolia.instantsearch.examples.android.showcase.compose.agent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.instantsearch.agent.chat.ChatStore
import com.algolia.instantsearch.agent.transport.AgentStudioTransport

/**
 * Owns the [ChatStore] for the Agent Studio showcase.
 *
 * Reuses the same Agent Studio showcase config that ships with the InstantSearch
 * web examples (`examples/js/showcase`), so the demo works out of the box.
 * In a real app, pass a **search-only** API key — never an admin key.
 */
class AgentChatViewModel : ViewModel() {

    val store: ChatStore = run {
        val transport = AgentStudioTransport.fromCredentials(
            appId = APP_ID,
            apiKey = SEARCH_API_KEY,
            agentId = AGENT_ID,
        )
        ChatStore(transport = transport, scope = viewModelScope)
    }

    fun send(text: String) = store.send(text)

    fun stop() = store.stop()

    override fun onCleared() {
        super.onCleared()
        store.stop()
    }

    private companion object {
        const val APP_ID = "latency"
        const val SEARCH_API_KEY = "6be0576ff61c053d5f9a3225e2a90f76"
        const val AGENT_ID = "eedef238-5468-470d-bc37-f99fa741bd25"
    }
}
