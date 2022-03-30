package com.algolia.instantsearch.showcase.compose.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize

class SearchOnSubmitShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val hitsState = HitsState<Movie>()

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.OnSubmit)
    private val connections = ConnectionHandler(searchBox)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += searcher.connectHitsView(hitsState) { it.hits.deserialize(Movie.serializer()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                SearchAsYouTypeScreen(
                    moviesList = hitsState.hits,
                    searchBoxState = searchBoxState
                )
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun SearchAsYouTypeScreen(
        moviesList: List<Movie>,
        searchBoxState: SearchBoxState
    ) {
        val moviesListState = rememberLazyListState()
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed,
                    lazyListState = moviesListState
                )
            },
            content = {
                MoviesList(
                    modifier = Modifier.fillMaxWidth(),
                    movies = moviesList,
                    listState = moviesListState
                )
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
