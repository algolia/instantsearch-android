package com.algolia.instantsearch.showcase.suggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.loading.LoadingConnector
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.showcase.list.suggestion.Suggestion
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class QuerySuggestionViewModel : ViewModel() {

    private val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("927c3fe76d4b52c5a2912973f35a3077"),
        logLevel = LogLevel.ALL
    )
    val multiSearcher = MultiSearcher(client)
    val productSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("STAGING_native_ecom_demo_products")
    )
    val suggestionSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("STAGING_native_ecom_demo_products_query_suggestions")
    )

    val searchBox = SearchBoxConnector(multiSearcher)
    val loading = LoadingConnector(multiSearcher)

    private val connection = ConnectionHandler(searchBox, loading)

    val suggestions = MutableLiveData<Suggestion>()

    override fun onCleared() {
        multiSearcher.cancel()
        connection.clear()
        client.close()
    }
}
