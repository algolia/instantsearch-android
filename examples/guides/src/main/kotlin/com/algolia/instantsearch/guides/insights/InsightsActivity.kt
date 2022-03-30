package com.algolia.instantsearch.guides.insights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.guides.insights.extension.configureSearchView
import com.algolia.instantsearch.guides.model.Product
import com.algolia.instantsearch.insights.sharedInsights
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.tracker.HitsTracker
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.EventName
import io.ktor.client.features.logging.*

class InsightsActivity : AppCompatActivity() {

    private val indexName = IndexName("instant_search")
    private val insights = sharedInsights(indexName)
    private val client = ClientSearch(
        applicationID = insights.applicationID,
        apiKey = insights.apiKey,
        logLevel = LogLevel.ALL
    )
    private val searcher = HitsSearcher(client, indexName)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val hitsTracker = HitsTracker(
        eventName = EventName("demo"),
        searcher = searcher,
        insights = insights
    )
    private val connection = ConnectionHandler(searchBox, hitsTracker)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)

        val adapter = ListItemAdapter(hitsTracker)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        configureSearchView(searchView, resources.getString(R.string.search_items))
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.configure(adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
