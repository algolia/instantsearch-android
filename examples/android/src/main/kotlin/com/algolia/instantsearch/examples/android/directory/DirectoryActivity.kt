package com.algolia.instantsearch.examples.android.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.examples.android.guides.extension.configure
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch

class DirectoryActivity : AppCompatActivity() {

    private val client = ClientSearch(
        appId = "latency",
        apiKey = "1f6fd3a6fb973cb08419fe7d288fa4db",
    )
    private val searcher = HitsSearcher(client = client, indexName = "mobile_demos")
    private val connector = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(connector)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        connection += connector.connectView(SearchBoxViewAppCompat(findViewById(R.id.searchView)))

        val adapter = DirectoryAdapter()
        connection += searcher.connectHitsView(adapter) { directoryItems(it, guides) }

        findViewById<RecyclerView>(R.id.list).configure(adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
