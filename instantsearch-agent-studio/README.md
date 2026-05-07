# instantsearch-agent-studio

Minimal native client for [Algolia Agent Studio][1]. Mirrors the AI SDK 5
wire format used by `react-instantsearch`'s `<Chat>` widget — without bundling
any chat UI. Bring-your-own Compose / View code.


## Install

`build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.algolia:instantsearch-agent-studio:0.1.0")
    implementation("io.ktor:ktor-client-okhttp:3.3.3") // or your preferred engine
}
```

## Quick start (Jetpack Compose + ViewModel)

```kotlin
class AgentChatViewModel(
    appId: String,
    apiKey: String,
    agentId: String,
) : ViewModel() {
    private val transport = AgentStudioTransport.fromCredentials(
        appId = appId,
        apiKey = apiKey,
        agentId = agentId,
    )
    val store = ChatStore(transport = transport, scope = viewModelScope)
}

@Composable
fun AgentChatScreen(viewModel: AgentChatViewModel) {
    val messages by viewModel.store.messages.collectAsState()
    val status by viewModel.store.status.collectAsState()
    val error by viewModel.store.error.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(messages) { MessageRow(it) }
            if (status == ChatStatus.Submitted || status == ChatStatus.Streaming) {
                item { CircularProgressIndicator(Modifier.padding(8.dp)) }
            }
        }

        error?.let {
            Text(
                text = it.message ?: "Error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Row(Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask anything…") },
            )
            Spacer(Modifier.width(8.dp))
            Button(
                enabled = input.isNotBlank() && status == ChatStatus.Ready,
                onClick = {
                    viewModel.store.send(input.trim())
                    input = ""
                },
            ) { Text("Send") }
            if (status == ChatStatus.Streaming) {
                IconButton(onClick = viewModel.store::stop) {
                    Icon(Icons.Default.Close, contentDescription = "Stop")
                }
            }
        }
    }
}

@Composable
private fun MessageRow(message: UIMessage) {
    val prefix = if (message.role == MessageRole.User) "🧑" else "🤖"
    Column {
        Text("$prefix ${message.plainText}")
        message.parts.filterIsInstance<UIMessagePart.Tool>().forEach { tool ->
            ToolCallChip(tool.part)
        }
    }
}

@Composable
private fun ToolCallChip(part: ToolUIPart) {
    val label = when (val state = part.state) {
        is ToolCallState.InputStreaming -> "preparing…"
        is ToolCallState.InputAvailable -> "running…"
        is ToolCallState.OutputAvailable -> "done"
        is ToolCallState.OutputError -> "error: ${state.errorText}"
    }
    AssistChip(onClick = {}, label = { Text("${part.toolName} • $label") })
}
```

## What's in v0.1

| | |
|---|---|
| `AgentStudioEndpoint` | Builds the `agent-studio/1/agents/{id}/completions?compatibilityMode=ai-sdk-5` URL. |
| `AgentStudioTransport` | Ktor-backed POST + SSE response. |
| `SseEventStream` | `Flow<UIMessageChunk>` from a Ktor `ByteReadChannel`. |
| `ChatStore` | StateFlow-backed aggregator exposing `messages`, `status`, `error`, `send`/`regenerate`/`stop`/`clear`. |

