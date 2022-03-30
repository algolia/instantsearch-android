package com.algolia.instantsearch.guides.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName

class DirectoryActivity : AppCompatActivity() {

    private val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
    )
    private val searcher = HitsSearcher(client = client, indexName = IndexName("mobile_guides"))
    private val connector = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(connector)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        connection += connector.connectView(SearchBoxViewAppCompat(findViewById(R.id.searchView)))

        val adapter = DirectoryAdapter()
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(DirectoryHit.serializer())
                .filter { guides.containsKey(it.objectID) }
                .groupBy { it.type }
                .toSortedMap()
                .flatMap { (key, value) ->
                    listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it) }
                        .sortedBy { it.hit.objectID.raw }
                }
        }

        findViewById<RecyclerView>(R.id.list).configure(adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
