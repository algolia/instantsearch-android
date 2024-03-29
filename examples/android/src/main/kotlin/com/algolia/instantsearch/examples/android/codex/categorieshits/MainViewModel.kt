package com.algolia.instantsearch.examples.android.codex.categorieshits

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.facets.addFacetsSearcher
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet

class MainViewModel : ViewModel() {

    private val multiSearcher = MultiSearcher(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("6be0576ff61c053d5f9a3225e2a90f76")
    )
    private val indexName = IndexName("instant_search")
    private val attribute = Attribute("categories")
    private val productsSearcher = multiSearcher.addHitsSearcher(indexName)
    private val categoriesSearcher = multiSearcher.addFacetsSearcher(indexName, attribute)
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val categoriesState = HitsState<Facet>()
    val productsState = HitsState<Product>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += categoriesSearcher.connectHitsView(categoriesState) { it.facets }
        connections += productsSearcher.connectHitsView(productsState) { it.hits.deserialize(Product.serializer()) }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
