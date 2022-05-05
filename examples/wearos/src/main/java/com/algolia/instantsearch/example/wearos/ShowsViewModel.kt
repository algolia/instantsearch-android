package com.algolia.instantsearch.example.wearos

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel

class ShowsViewModel : ViewModel() {
    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("3832e8fcaf80b1c7085c59fa3e4d266d"),
        LogLevel.ALL
    )
    val searcher = HitsSearcher(client, IndexName("tmdb_movies_shows"))
    private val connections = ConnectionHandler()

    init {
        // requesting only needed attributes
        searcher.query.attributesToRetrieve = Show.attributes
    }

    fun search(query: String) {
        searcher.setQuery(query)
        searcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        connections.disconnect()
    }
}
