package com.algolia.instantsearch.examples.showcase.compose.directory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.directory.DirectoryItem
import com.algolia.instantsearch.examples.directory.directoryItems
import com.algolia.instantsearch.examples.directory.navigateTo
import com.algolia.instantsearch.examples.showcase.compose.client
import com.algolia.instantsearch.examples.showcase.compose.configureSearchBox
import com.algolia.instantsearch.examples.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class ComposeDirectoryShowcase : ComponentActivity() {

    private val indexName = IndexName("mobile_demo_home")
    private val searcher = HitsSearcher(client, indexName, Query(hitsPerPage = 100))
    private val hitsState = HitsState<DirectoryItem>(emptyList())
    private val searchBoxState = SearchBoxState()
    private val connections = ConnectionHandler()

    init {
        connections += searcher.connectHitsView(hitsState) { directoryItems(it, showcases) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                Directory(searchBoxState, hitsState, ::navigateTo)
            }
        }
        configureSearchBox(searcher, searchBoxState, connections)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
