package com.algolia.instantsearch.examples.android.showcase.compose.agent

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.algolia.instantsearch.agent.model.ChatStatus
import com.algolia.instantsearch.agent.model.MessageRole
import com.algolia.instantsearch.agent.model.ToolCallState
import com.algolia.instantsearch.agent.model.ToolUIPart
import com.algolia.instantsearch.agent.model.UIMessage
import com.algolia.instantsearch.agent.model.UIMessagePart
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * Showcase for the experimental, standalone `instantsearch-agent` SDK.
 *
 * Demonstrates the minimal flow: build an [com.algolia.instantsearch.agent.transport.AgentStudioTransport]
 * from credentials, drive a [com.algolia.instantsearch.agent.chat.ChatStore] from a
 * [androidx.lifecycle.ViewModel], and render its observable state with Compose.
 *
 * It reuses the Agent Studio showcase agent that ships with the InstantSearch
 * web examples, so it works out of the box with no setup.
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
    val suggestions by store.suggestions.collectAsState()

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

            // Prompt suggestions ("what to ask next"). Shown only while idle, as
            // tappable chips; clicking one sends it as a new user message — the
            // same behavior as the web Chat widget.
            if (status == ChatStatus.Ready && suggestions.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(suggestions, key = { it }) { suggestion ->
                        SuggestionChip(text = suggestion, onClick = { viewModel.send(suggestion) })
                    }
                }
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
                    enabled = input.isNotBlank() && status == ChatStatus.Ready,
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
private fun SuggestionChip(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(alpha = 0.5f)),
        elevation = 0.dp,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
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
        if (message.plainText.isNotEmpty()) {
            Text(text = message.plainText)
        }
        message.parts.filterIsInstance<UIMessagePart.Tool>().forEach { tool ->
            ToolPart(tool.part)
        }
    }
}

@Composable
private fun ToolPart(part: ToolUIPart) {
    when (val state = part.state) {
        is ToolCallState.OutputAvailable -> {
            // Mirrors the web Chat widget: the `algolia_search_index` tool
            // returns `output.hits`, which we render as a product carousel.
            val products = if (isSearchTool(part.toolName)) {
                extractProducts(state.output)
            } else {
                emptyList()
            }
            if (products.isNotEmpty()) {
                ProductCarousel(products)
            } else {
                ToolStatusLabel(part.toolName, "done")
            }
        }
        is ToolCallState.OutputError -> ToolStatusLabel(part.toolName, "error: ${state.errorText}")
        is ToolCallState.InputAvailable -> ToolStatusLabel(part.toolName, "running…")
        is ToolCallState.InputStreaming -> ToolStatusLabel(part.toolName, "preparing…")
    }
}

@Composable
private fun ToolStatusLabel(toolName: String, status: String) {
    Text(
        text = "🔧 $toolName • $status",
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.padding(top = 2.dp),
    )
}

@Composable
private fun ProductCarousel(products: List<Product>) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(products, key = { it.objectID }) { ProductCard(it) }
    }
}

@Composable
private fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center,
            ) {
                if (product.imageUrl != null) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.size(120.dp),
                    )
                } else {
                    Text("🛍️")
                }
            }
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                product.price?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary,
                    )
                }
            }
        }
    }
}

private data class Product(
    val objectID: String,
    val name: String,
    val imageUrl: String?,
    val price: String?,
)

/** The web showcase treats `algolia_search_index` and `algolia_search_index_*` (MCP) as product search. */
private fun isSearchTool(toolName: String): Boolean =
    toolName == "algolia_search_index" || toolName.startsWith("algolia_search_index_")

/** Reads `output.hits[]` from the search tool result, matching the web Chat widget. */
private fun extractProducts(output: kotlinx.serialization.json.JsonElement): List<Product> {
    val hits = (output as? JsonObject)?.get("hits") as? JsonArray ?: return emptyList()
    return hits.mapNotNull { element ->
        val hit = element as? JsonObject ?: return@mapNotNull null
        val objectID = hit.string("objectID") ?: return@mapNotNull null
        Product(
            objectID = objectID,
            name = hit.string("name") ?: hit.string("title") ?: objectID,
            imageUrl = hit.string("image") ?: hit.string("image_url") ?: hit.string("thumbnailUrl"),
            price = hit.number("price")?.let { "$$it" },
        )
    }
}

private fun JsonObject.string(key: String): String? =
    (this[key] as? JsonPrimitive)?.contentOrNull?.takeIf { it.isNotBlank() }

private fun JsonObject.number(key: String): String? {
    val primitive = this[key] as? JsonPrimitive ?: return null
    return primitive.contentOrNull?.takeIf { it.isNotBlank() && it.toDoubleOrNull() != null }
}
