package com.algolia.instantsearch.showcase.compose.list.merged

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.model.Actor
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.ActorsList
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class MergedListShowcase : AppCompatActivity() {


    private val multiSearcher = MultiSearcher(client)
    private val moviesSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("mobile_demo_movies"),
        query = Query(hitsPerPage = 3)
    )
    private val actorsSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("mobile_demo_actors"),
        query = Query(hitsPerPage = 5)
    )
    private val searchBox = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBox)

    private val searchBoxState = SearchBoxState()
    private val actorsState = HitsState<Actor>()
    private val moviesState = HitsState<Movie>()

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += actorsSearcher.connectHitsView(actorsState) { it.hits.deserialize(Actor.serializer()) }
        connections += moviesSearcher.connectHitsView(moviesState) { it.hits.deserialize(Movie.serializer()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                MergedListScreen(actorsState, moviesState)
            }
        }
        multiSearcher.searchAsync()
    }

    @Composable
    fun MergedListScreen(
        actorsState: HitsState<Actor>,
        moviesState: HitsState<Movie>,
    ) {
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Actors",
                        style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ActorsList(actors = actorsState.hits)
                    Text(
                        text = "Movies", style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    MoviesList(movies = moviesState.hits)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connections.clear()
    }
}
