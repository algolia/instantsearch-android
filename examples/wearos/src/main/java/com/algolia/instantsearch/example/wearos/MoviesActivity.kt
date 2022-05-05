package com.algolia.instantsearch.example.wearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel

class MoviesActivity : ComponentActivity() {

    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("3832e8fcaf80b1c7085c59fa3e4d266d"),
        LogLevel.ALL
    )
    private val searcher = HitsSearcher(client, IndexName("tmdb_movies_shows"))
    private val connections = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        val movieAdapter = MovieAdapter()
        connections += searcher.connectHitsView(movieAdapter) {
            it.hits.deserialize(Movie.serializer())
        }

        findViewById<WearableRecyclerView>(R.id.list).apply {
            adapter = movieAdapter
            layoutManager = WearableLinearLayoutManager(this@MoviesActivity, CustomScrollingLayoutCallback())
        }

        val query = intent.getStringExtra(ExtraQuery) ?: ""
        searcher.query.attributesToRetrieve = Movie.attributes
        searcher.setQuery(query)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        connections.clear()
    }

    companion object {
        const val ExtraQuery = "query"
    }
}
