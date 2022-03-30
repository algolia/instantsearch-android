package com.algolia.instantsearch.showcase.compose.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.component.AutoCompleteTextField
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.helper.deserialize

class SearchAutoCompleteShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val hitsState = HitsState<String>()
    private val connections = ConnectionHandler(searchBox)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += searcher.connectHitsView(hitsState) {
            it.hits.deserialize(Movie.serializer()).map(Movie::title)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAutoCompleteScreen(searchBoxState = searchBoxState)
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    private fun SearchAutoCompleteScreen(
        searchBoxState: SearchBoxState
    ) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                AutoCompleteTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    searchBoxState = searchBoxState,
                    label = "Search for items",
                    suggestions = hitsState.hits
                )
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
