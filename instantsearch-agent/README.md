# instantsearch-agent

> [!WARNING]
> **Experimental.** This is a standalone, early-stage library. It is released
> alongside the main InstantSearch Android library and shares its version
> number, but its public API is annotated with `@ExperimentalAgentStudioApi`
> and can change in source- and binary-incompatible ways — including being
> renamed or removed — in any release. It is a *beta feature* per
> [Algolia's Terms of Service ("Beta Services")](https://www.algolia.com/policies/terms/).

Minimal native client for [Algolia Agent Studio][1]. Mirrors the AI SDK 5
wire format used by `react-instantsearch`'s `<Chat>` widget — without bundling
any chat UI. Bring-your-own Compose / View code.

Full documentation: see the [InstantSearch Agent Studio guide][2] on the
Algolia docs.

## Install

`build.gradle.kts`:

```kotlin
dependencies {
    // Same version as the other InstantSearch Android artifacts.
    implementation("com.algolia:instantsearch-agent:5.0.0")
    implementation("io.ktor:ktor-client-okhttp:3.3.3") // or your preferred engine
}
```

## Opting in to the experimental API

Every public declaration requires an explicit opt-in. Either annotate the
usage site with `@OptIn(ExperimentalAgentStudioApi::class)`, or opt in
module-wide in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets.all {
        languageSettings.optIn("com.algolia.instantsearch.agent.ExperimentalAgentStudioApi")
    }
}
```

## Quick start (Jetpack Compose + ViewModel)

```kotlin
@OptIn(ExperimentalAgentStudioApi::class)
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

## What's included

| | |
|---|---|
| `AgentStudioEndpoint` | Builds the `agent-studio/1/agents/{id}/completions?compatibilityMode=ai-sdk-5` URL. |
| `AgentStudioTransport` | Ktor-backed POST + SSE response. |
| `SseEventStream` | `Flow<UIMessageChunk>` from a Ktor `ByteReadChannel`. |
| `ChatStore` | StateFlow-backed aggregator exposing `messages`, `status`, `error`, `send`/`regenerate`/`stop`/`clear`. |

## What's not yet here (planned)

- Drop-in Compose chat composable.
- Client-side tool execution (`onToolCall`).
- Conversation persistence (DataStore / Room).
- Suggestion data parts surfaced as a typed property.
- Feedback (👍 / 👎) endpoint.

## Versioning

This library is published with every InstantSearch Android release and uses the
**same version number** as the other `com.algolia:instantsearch-*` artifacts
(root `VERSION_NAME`). Because the API is experimental, breaking changes can
land in any release, including patch releases; the `@ExperimentalAgentStudioApi`
opt-in is the stability signal, not the version number.

Releases up to and including `0.1.0` used a separate `0.x` version line; from
`4.1.1` onwards the module follows the main version.

### Changelog

Changes are tracked in the repository's top-level [CHANGELOG](../CHANGELOG.md).

#### 0.1.0

- Initial experimental release: `AgentStudioEndpoint`, `AgentStudioTransport`,
  `SseEventStream`, and `ChatStore` with AI SDK 5 streaming support.

[1]: https://www.algolia.com/doc/guides/algolia-ai/agent-studio/how-to/integration
[2]: https://www.algolia.com/doc/guides/algolia-ai/agent-studio/how-to/instantsearch-agent-android

