package com.algolia.instantsearch.examples.android.showcase.compose.composition

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.client.model.search.Hit
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.SearchTopBar
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.TitleTopBar
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.composition.CompositionSearcher
import com.algolia.instantsearch.stats.DefaultStatsPresenter
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.connectView
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * Showcase for [CompositionSearcher], the searcher targeting the Algolia
 * Composition API (`/1/compositions/{compositionID}/run` endpoint).
 *
 * A composition is configured per application in the Algolia dashboard, so
 * there is no public demo composition to hardcode here. The screen starts with
 * a small form asking for your application credentials and composition ID,
 * then drives the regular InstantSearch components (search box, stats, hits)
 * through a [CompositionSearcher].
 */
class CompositionShowcase : AppCompatActivity() {

    private var session by mutableStateOf<CompositionSession?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                when (val current = session) {
                    null -> CredentialsForm { applicationID, apiKey, compositionID ->
                        session = CompositionSession(applicationID, apiKey, compositionID)
                    }
                    else -> CompositionSearchScreen(current)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        session?.close()
    }
}

/**
 * Holds the searcher and its connections, created once the credentials are
 * submitted.
 */
private class CompositionSession(
    applicationID: String,
    apiKey: String,
    compositionID: String,
) {
    val searchBoxState = SearchBoxState()
    val statsText = StatsTextState()
    val hitsState = HitsState<CompositionHit>()
    var error by mutableStateOf<String?>(null)

    private val searcher = CompositionSearcher(
        applicationID = applicationID,
        apiKey = apiKey,
        compositionID = compositionID,
    )
    private val searchBox = SearchBoxConnector(searcher)
    private val stats = StatsConnector(searcher)
    private val connections = ConnectionHandler(searchBox, stats)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += stats.connectView(statsText, DefaultStatsPresenter())
        connections += searcher.connectHitsView(hitsState) { response ->
            response.hits.map { it.toCompositionHit() }
        }
        searcher.error.subscribe { error = it?.message }
        searcher.searchAsync()
    }

    fun close() {
        searcher.error.unsubscribeAll()
        searcher.cancel()
        connections.clear()
    }
}

/**
 * Composition records don't have a fixed schema, so the row displays the most
 * common "title" attributes and falls back to the objectID.
 */
private data class CompositionHit(
    val objectID: String,
    val title: String,
)

private fun Hit.toCompositionHit(): CompositionHit {
    val title = listOf("name", "title", "label")
        .firstNotNullOfOrNull { attribute ->
            (additionalProperties?.get(attribute) as? JsonPrimitive)?.contentOrNull
        }
    return CompositionHit(objectID = objectID, title = title ?: objectID)
}

@Composable
private fun CredentialsForm(onSubmit: (String, String, String) -> Unit) {
    var applicationID by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var compositionID by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TitleTopBar(title = "Composition (experimental)") },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Compositions are configured per application in the Algolia dashboard. " +
                    "Enter the credentials of an application with at least one composition.",
                style = MaterialTheme.typography.body2,
            )
            OutlinedTextField(
                value = applicationID,
                onValueChange = { applicationID = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Application ID") },
                singleLine = true,
            )
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search-only API Key") },
                singleLine = true,
            )
            OutlinedTextField(
                value = compositionID,
                onValueChange = { compositionID = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Composition ID") },
                singleLine = true,
            )
            Button(
                enabled = applicationID.isNotBlank() && apiKey.isNotBlank() && compositionID.isNotBlank(),
                onClick = { onSubmit(applicationID.trim(), apiKey.trim(), compositionID.trim()) },
            ) {
                Text("Start searching")
            }
        }
    }
}

@Composable
private fun CompositionSearchScreen(session: CompositionSession) {
    val listState = rememberLazyListState()
    Scaffold(
        topBar = {
            SearchTopBar(
                placeHolderText = "Search in your composition",
                searchBoxState = session.searchBoxState,
                lazyListState = listState,
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                text = session.statsText.stats,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            session.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
            LazyColumn(state = listState) {
                items(session.hitsState.hits) { hit ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(text = hit.title, style = MaterialTheme.typography.body1)
                        Text(
                            text = hit.objectID,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        )
                    }
                    Divider()
                }
            }
        }
    }
}
