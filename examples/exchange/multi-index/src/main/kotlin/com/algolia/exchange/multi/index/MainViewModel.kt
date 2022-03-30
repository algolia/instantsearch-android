package com.algolia.exchange.multi.index

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class MainViewModel : ViewModel() {

    private val multiSearcher = MultiSearcher(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
    )
    private val actorsSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_actors"), Query(hitsPerPage = 5))
    private val moviesSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_movies"))
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val actorsState = HitsState<Actor>()
    val moviesState = HitsState<Movie>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += actorsSearcher.connectHitsView(actorsState) { it.hits.deserialize(Actor.serializer()) }
        connections += moviesSearcher.connectHitsView(moviesState) { it.hits.deserialize(Movie.serializer()) }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
