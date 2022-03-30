package com.algolia.instantsearch.showcase.compose.stats

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.item.StatsState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar

class StatsShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val searchBox = SearchBoxConnector(searcher)
    private val searchBoxState = SearchBoxState()

    private val stats = StatsConnector(searcher)
    private val statsA = StatsTextState()
    private val statsB = StatsState(AnnotatedString(""))

    private val connections = ConnectionHandler(stats, searchBox)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += stats.connectView(statsA, StatsPresenterImpl())
        connections += stats.connectView(statsB) { response ->
            with(AnnotatedString.Builder()) {
                if (response != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(response.nbHits.toString())
                    }
                    append(" ${getString(R.string.hits)}")
                    val query = searcher.query.query
                    if (query != null && query.isNotBlank()) {
                        append(" for \"$query\"")
                    }
                }
                toAnnotatedString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                StatsScreen()
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun StatsScreen() {
        Scaffold(
            topBar = {
                SearchTopBar(
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = statsA.stats,
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = statsB.stats,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}