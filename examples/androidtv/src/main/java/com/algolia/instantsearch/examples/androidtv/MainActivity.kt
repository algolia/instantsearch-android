package com.algolia.instantsearch.examples.androidtv

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.fragment.app.FragmentActivity
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName

class MainActivity : FragmentActivity() {

    private val searcher = HitsSearcher(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("3832e8fcaf80b1c7085c59fa3e4d266d"),
        indexName = IndexName("tmdb_movies_shows")
    )
    private val searchBoxState = SearchBoxState()
    private val hitsState = HitsState<Show>()

    private val searchBoxConnector = SearchBoxConnector(searcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SearchScreen(searchBoxState = searchBoxState, hitsState = hitsState)
            }
        }

        connections += searchBoxConnector.connectView(searchBoxState)
        connections += searcher.connectHitsView(hitsState) {
            it.hits.deserialize(Show.serializer())
        }
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        connections.clear()
    }
}
