package com.algolia.instantsearch.examples.android.codex.multisearch

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.model.search.Query

class MainViewModel : ViewModel() {

    private val multiSearcher = MultiSearcher(
        appId = "latency",
        apiKey = "1f6fd3a6fb973cb08419fe7d288fa4db"
    )
    private val actorsSearcher = multiSearcher.addHitsSearcher("mobile_demo_actors", Query(hitsPerPage = 5))
    private val moviesSearcher = multiSearcher.addHitsSearcher("mobile_demo_movies")
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    init {
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
