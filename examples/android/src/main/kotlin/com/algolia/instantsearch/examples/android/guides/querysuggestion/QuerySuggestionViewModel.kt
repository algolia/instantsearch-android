package com.algolia.instantsearch.examples.android.guides.querysuggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.examples.android.guides.model.Suggestion
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName

class QuerySuggestionViewModel : ViewModel() {

    private val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("927c3fe76d4b52c5a2912973f35a3077"),
        logLevel = LogLevel.All
    )
    val multiSearcher = MultiSearcher(client)
    val productSearcher = multiSearcher.addHitsSearcher(indexName = IndexName("STAGING_native_ecom_demo_products"))
    val suggestionSearcher =
        multiSearcher.addHitsSearcher(indexName = IndexName("STAGING_native_ecom_demo_products_query_suggestions"))
    val searchBox = SearchBoxConnector(multiSearcher)
    val suggestions = MutableLiveData<Suggestion>()
    val connections = ConnectionHandler()

    init {
        searchBox.connect()
    }

    override fun onCleared() {
        searchBox.disconnect()
        connections.clear()
        multiSearcher.cancel()
        client.close()
    }
}
