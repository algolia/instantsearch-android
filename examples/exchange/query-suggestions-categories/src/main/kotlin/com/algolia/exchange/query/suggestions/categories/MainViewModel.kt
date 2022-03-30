package com.algolia.exchange.query.suggestions.categories

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
        apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
    )
    private val suggestionsSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("instantsearch_query_suggestions")
    )
    private val attribute = Attribute("categories")
    private val categoriesSearcher = multiSearcher.addFacetsSearcher(
        indexName = IndexName("instant_search"),
        attribute = attribute
    )
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)

    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val categoriesState = HitsState<Facet>()
    val suggestionsState = HitsState<Suggestion>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += categoriesSearcher.connectHitsView(categoriesState) { it.facets }
        connections += suggestionsSearcher.connectHitsView(suggestionsState) {
            it.hits.deserialize(
                Suggestion.serializer()
            )
        }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
