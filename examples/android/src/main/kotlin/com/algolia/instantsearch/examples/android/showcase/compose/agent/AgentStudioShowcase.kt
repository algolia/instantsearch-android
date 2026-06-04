package com.algolia.instantsearch.examples.android.showcase.compose.agent

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.agent.model.ChatStatus
import com.algolia.instantsearch.agent.model.MessageRole
import com.algolia.instantsearch.agent.model.ToolCallState
import com.algolia.instantsearch.agent.model.UIMessage
import com.algolia.instantsearch.agent.model.UIMessagePart
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme

/**
 * Showcase for the experimental, standalone `instantsearch-agent` SDK.
 *
 * Demonstrates the minimal flow: build an [com.algolia.instantsearch.agent.transport.AgentStudioTransport]
 * from credentials, drive a [com.algolia.instantsearch.agent.chat.ChatStore] from a
 * [androidx.lifecycle.ViewModel], and render its observable state with Compose.
 *
 * The agent id is entered at runtime so the demo can run against any published
 * Agent Studio agent without hardcoding one.
 */
class AgentStudioShowcase : AppCompatActivity() {

    private val viewModel: AgentChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                AgentScreen(viewModel)
            }
        }
    }
}

@Composable
private fun AgentScreen(viewModel: AgentChatViewModel) {
    val store = viewModel.store
    val messages by store.messages.collectAsState()
    val status by store.status.collectAsState()
    val error by store.error.collectAsState()

    var agentId by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agent Studio (experimental)") }) },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            OutlinedTextField(
                value = agentId,
                onValueChange = {
                    agentId = it
                    viewModel.setAgentId(it.trim())
                },
                label = { Text("Agent ID") },
                placeholder = { Text("alg_…") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(messages, key = { it.id }) { MessageRow(it) }
                if (status == ChatStatus.Submitted || status == ChatStatus.Streaming) {
                    item { CircularProgressIndicator(Modifier.padding(8.dp)) }
                }
            }

            error?.let {
                Text(
                    text = it.message ?: "Error",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask anything…") },
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    enabled = input.isNotBlank() && agentId.isNotBlank() && status == ChatStatus.Ready,
                    onClick = {
                        viewModel.send(input.trim())
                        input = ""
                    },
                ) { Icon(Icons.Default.Send, contentDescription = "Send") }
                if (status == ChatStatus.Streaming) {
                    IconButton(onClick = viewModel::stop) {
                        Icon(Icons.Default.Close, contentDescription = "Stop")
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageRow(message: UIMessage) {
    val prefix = if (message.role == MessageRole.User) "You" else "Assistant"
    Column {
        Text(
            text = prefix,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
        )
        Text(text = message.plainText.ifEmpty { "…" })
        message.parts.filterIsInstance<UIMessagePart.Tool>().forEach { tool ->
            val label = when (val state = tool.part.state) {
                is ToolCallState.InputStreaming -> "preparing…"
                is ToolCallState.InputAvailable -> "running…"
                is ToolCallState.OutputAvailable -> "done"
                is ToolCallState.OutputError -> "error: ${state.errorText}"
            }
            Text(
                text = "🔧 ${tool.part.toolName} • $label",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
