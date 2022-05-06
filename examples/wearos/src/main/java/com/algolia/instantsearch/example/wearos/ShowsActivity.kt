package com.algolia.instantsearch.example.wearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.example.wearos.internal.CustomScrollingLayoutCallback
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName

class ShowsActivity : ComponentActivity() {

    private val searcher = HitsSearcher(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("3832e8fcaf80b1c7085c59fa3e4d266d"),
        indexName = IndexName("tmdb_movies_shows")
    )
    private val connections = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        val showAdapter = ShowAdapter()
        findViewById<WearableRecyclerView>(R.id.list).apply {
            adapter = showAdapter
            layoutManager = WearableLinearLayoutManager(this@ShowsActivity, CustomScrollingLayoutCallback())
        }

        connections += searcher.connectHitsView(showAdapter) {
            it.hits.deserialize(Show.serializer())
        }

        searcher.query.attributesToRetrieve = Show.attributes
        searcher.setQuery(query)
        searcher.searchAsync()
    }

    private val query: String
        get() = intent.getStringExtra(ExtraQuery) ?: ""

    override fun onDestroy() {
        super.onDestroy()
        connections.clear()
    }

    companion object {
        const val ExtraQuery = "query"
    }
}
